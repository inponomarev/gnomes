package ee.lagao.clickmatters.codegen;

import com.squareup.javapoet.*;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.lang.model.element.Modifier;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class APIExtractor implements TypeSpecExtractor {
    private final TypeDefiner typeDefiner;

    public APIExtractor(TypeDefiner typeDefiner) {
        this.typeDefiner = typeDefiner;
    }

    @Override
    public void extractTypeSpecs(OpenAPI openAPI, BiConsumer<ClassCategory, TypeSpec> typeSpecBiConsumer) {
        Paths paths = openAPI.getPaths();
        if (paths == null) return;
        TypeSpec.Builder classBuilder = TypeSpec.interfaceBuilder("Controller");
        for (Map.Entry<String, PathItem> stringPathItemEntry : paths.entrySet()) {
            for (Map.Entry<PathItem.HttpMethod, Operation> operationEntry : stringPathItemEntry.getValue().readOperationsMap().entrySet()) {
                MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(operationEntry.getValue().getOperationId())
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
                PathItem.HttpMethod httpMethod = operationEntry.getKey();
                methodBuilder.addAnnotation(getControllerMethodAnnotationSpec(httpMethod, stringPathItemEntry.getKey()));
                //we are deriving the returning type from the schema of the successful result
                methodBuilder.returns(determineReturnJavaType(operationEntry.getValue(), classBuilder));
                Optional.ofNullable(operationEntry.getValue().getRequestBody()).map(RequestBody::getContent)
                        .map(c -> getContentType(c, classBuilder)).ifPresent(typeName ->
                        methodBuilder.addParameter(ParameterSpec.builder(
                                typeName,
                                "request")
                                .addAnnotation(org.springframework.web.bind.annotation.RequestBody.class).build())
                );
                methodBuilder.addParameter(ParameterSpec.builder(
                        HttpServletResponse.class,
                        "response").build());
                classBuilder.addMethod(methodBuilder.build());

            }

        }
        typeSpecBiConsumer.accept(ClassCategory.CONTROLLER, classBuilder.build());
    }

    private TypeName determineReturnJavaType(Operation operation, TypeSpec.Builder parent) {
        for (Map.Entry<String, ApiResponse> responseEntry : operation.getResponses().entrySet()) {
            if (responseEntry.getKey().matches("2\\d\\d")) {
                Content content = responseEntry.getValue().getContent();
                return getContentType(content, parent);
            }
        }
        return TypeName.VOID;
    }

    private TypeName getContentType(Content content, TypeSpec.Builder parent) {
        return Optional.ofNullable(content)
                .flatMap(c -> Optional.ofNullable(c.get("application/json"))
                        .or(() -> Optional.of(c.get("*/*"))))
                .map(MediaType::getSchema)
                .map(s -> typeDefiner.defineJavaType(s, parent))
                .orElse(TypeName.VOID);
    }

    private AnnotationSpec getControllerMethodAnnotationSpec(PathItem.HttpMethod httpMethod, String path) {
        Class<?> annotationClass = null;
        switch (httpMethod) {
            case GET:
                annotationClass = GetMapping.class;
                break;
            case POST:
                annotationClass = PostMapping.class;
                break;
            case PUT:
                annotationClass = PutMapping.class;
                break;
        }
        if (annotationClass != null) {
            return AnnotationSpec.builder(annotationClass)
                    .addMember("value", "$S", path)
                    .build();
        } else return null;

    }
}
