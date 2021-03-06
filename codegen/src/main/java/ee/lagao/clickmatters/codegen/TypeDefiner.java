package ee.lagao.clickmatters.codegen;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.squareup.javapoet.*;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import lombok.Data;
import org.apache.commons.text.CaseUtils;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TypeDefiner {
    private final String rootPackage;
    private final BiConsumer<ClassCategory, TypeSpec> typeSpecBiConsumer;
    private boolean hasJsonZonedDateTimeDeserializer;

    public TypeDefiner(String rootPackage, BiConsumer<ClassCategory, TypeSpec> typeSpecBiConsumer) {
        this.rootPackage = rootPackage;
        this.typeSpecBiConsumer = typeSpecBiConsumer;
    }

    public TypeName defineJavaType(Schema<?> schema, TypeSpec.Builder parent) {
        String $ref = schema.get$ref();
        if ($ref == null) {
            String internalType = schema.getType();
            switch (internalType) {
                case "string":
                    if ("date".equals(schema.getFormat()))
                        return TypeName.get(LocalDate.class);
                    else if ("date-time".equals(schema.getFormat()))
                        return TypeName.get(ZonedDateTime.class);
                    else if ("uuid".equals(schema.getFormat()))
                        return ClassName.get(UUID.class);
                    else if (schema.getEnum() != null) {
                        //internal enum
                        String simpleName = schema.getTitle();
                        if (simpleName == null) {
                            throw new IllegalStateException("Inline enum schema must have a title");
                        }
                        TypeSpec.Builder enumBuilder = TypeSpec.enumBuilder(simpleName).addModifiers(Modifier.PUBLIC);
                        for (Object e : schema.getEnum()) {
                            enumBuilder.addEnumConstant(e.toString());
                        }
                        TypeSpec internalEnum = enumBuilder.build();
                        parent.addType(internalEnum);

                        return ClassName.get("", simpleName);
                    } else return ClassName.get(String.class);
                case "number":
                    if ("float".equals(schema.getFormat()))
                        return TypeName.FLOAT;
                    else
                        return TypeName.DOUBLE;
                case "integer":
                    if ("int64".equals(schema.getFormat()))
                        return TypeName.LONG;
                    else
                        return TypeName.INT;
                case "boolean":
                    return TypeName.BOOLEAN;
                case "array":
                    Schema<?> itemsSchema = ((ArraySchema) schema).getItems();
                    return ParameterizedTypeName.get(ClassName.get(List.class), defineJavaType(itemsSchema, parent));
                case "object":
                default:
                    String simpleName = schema.getTitle();
                    if (simpleName != null) {
                        typeSpecBiConsumer.accept(ClassCategory.DTO, getDTO(simpleName, schema));
                        return ClassName.get(String.join(".", rootPackage, "dto"),
                                simpleName);
                    } else {
                        //This means failure, in fact.
                        return ClassName.OBJECT;
                    }
            }
        } else {
            Matcher matcher = Pattern.compile("/([^/$]+)$").matcher($ref);
            matcher.find();
            return ClassName.get(String.join(".", rootPackage, "dto"), matcher.group(1));
        }
    }

    private void ensureJsonZonedDateTimeDeserializer() {
        if (!hasJsonZonedDateTimeDeserializer) {
            TypeSpec typeSpec =
                    TypeSpec.classBuilder("ZonedDateTimeDeserializer")
                            .superclass(ParameterizedTypeName.get(
                                    ClassName.get(JsonDeserializer.class),
                                    ClassName.get(ZonedDateTime.class)
                            ))
                            .addModifiers(Modifier.PUBLIC)
                            .addField(FieldSpec.builder(ClassName.get(DateTimeFormatter.class),
                                    "formatter")
                                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                                    .initializer("$T.ISO_OFFSET_DATE_TIME", DateTimeFormatter.class)
                                    .build())
                            .addMethod(MethodSpec.methodBuilder(
                                    "deserialize")
                                    .returns(ClassName.get(ZonedDateTime.class))
                                    .addAnnotation(Override.class)
                                    .addModifiers(Modifier.PUBLIC)
                                    .addParameter(ParameterSpec.builder(JsonParser.class, "jsonParser").build())
                                    .addParameter(ParameterSpec.builder(DeserializationContext.class, "deserializationContext").build())
                                    .addException(IOException.class)

                                    .addStatement("String date = jsonParser.getText()")
                                    .beginControlFlow("try ")
                                    .addStatement(
                                            "return $T.parse(date, formatter)", ZonedDateTime.class)
                                    .endControlFlow()
                                    .beginControlFlow("catch ($T e)", DateTimeException.class)
                                    .addStatement("throw new $T(jsonParser, e.getMessage())", JsonParseException.class)
                                    .endControlFlow()
                                    .build())
                            .build();
            typeSpecBiConsumer.accept(ClassCategory.DTO, typeSpec);
            hasJsonZonedDateTimeDeserializer = true;
        }
    }

    private TypeSpec getDTOClass(String name, Schema<?> schema) {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(name)
                .addAnnotation(Data.class)
                .addAnnotation(AnnotationSpec.builder(JsonNaming.class).addMember("value",
                        "$T.class", ClassName.get(PropertyNamingStrategy.SnakeCaseStrategy.class)).build())
                .addModifiers(Modifier.PUBLIC);
        //Add properties
        Map<String, Schema> schemaMap = schema.getProperties();
        if (schemaMap != null)
            for (Map.Entry<String, Schema> entry : schemaMap.entrySet()) {
                if (!entry.getKey().matches("[a-z][a-z_0-9]*")) throw new IllegalStateException(
                        String.format("Property '%s' of schema '%s' is not in snake case",
                                entry.getKey(), name)
                );
                TypeName typeName = defineJavaType(entry.getValue(), classBuilder);
                FieldSpec.Builder fieldBuilder = FieldSpec.builder(
                        typeName,
                        CaseUtils.toCamelCase(entry.getKey(), false, '_'), Modifier.PRIVATE);
                if (typeName instanceof ClassName && "ZonedDateTime"
                        .equals(((ClassName) typeName).simpleName())) {
                    fieldBuilder.addAnnotation(AnnotationSpec.builder(
                            ClassName.get(JsonDeserialize.class))
                            .addMember("using", "ZonedDateTimeDeserializer.class")
                            .build());
                    ensureJsonZonedDateTimeDeserializer();
                }
                FieldSpec fieldSpec = fieldBuilder.build();
                classBuilder.addField(fieldSpec);
            }
        return classBuilder.build();
    }

    private TypeSpec getEnum(String name, Schema<?> schema) {
        TypeSpec.Builder classBuilder = TypeSpec.enumBuilder(name).addModifiers(Modifier.PUBLIC);
        for (Object val : schema.getEnum()) {
            classBuilder.addEnumConstant(val.toString());
        }
        return classBuilder.build();
    }

    TypeSpec getDTO(String name, Schema<?> schema) {
        if (schema.getEnum() != null)
            return getEnum(name, schema);
        else
            return getDTOClass(name, schema);

    }
}
