package ee.lagao.clickmatters.codegen;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mojo(
        name = "gen-server",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES
)
public class CodegenMojo extends AbstractMojo {
    @Parameter(property = "spec", required = true)
    String spec;

    @Parameter(property = "rootPackage", required = true)
    String rootPackage;

    @Component
    MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {
        Codegen codegen = new Codegen(rootPackage);
        try {
            Path targetPath = getTargetPath();
            codegen.generate(Path.of(spec), targetPath);
            project.addCompileSourceRoot(targetPath.toString());
        } catch (IOException e) {
            throw new MojoExecutionException("Generation failed", e);
        }


    }

    private Path getTargetPath() throws IOException {
        Path result = Path.of(project.getBuild().getDirectory()
                + File.separator + "generated-sources" + File.separator + "openapi");
        if (!Files.exists(result)){
            Files.createDirectories(result);
        }
        return result;
    }
}
