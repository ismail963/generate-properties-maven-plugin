import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * Created by ielfaqir on 13/04/2017.
 */

@Mojo(name = "clean")
public class CleanMojo extends AbstractMojo {
    @Parameter(property = "generate.origin")
    private String origin;

    @Parameter(property = "generate.destination")
    private String destination;

    public void execute() throws MojoExecutionException {
        try {
            getLog().info("Cleaning destinations files ... ");
            PropertyFiles dest = new PropertyFiles(destination);
            for (File file : dest.getProperties()) {
                file.delete();
            }
            getLog().info("Destination cleaned");

        } catch (PropertyException e) {
            getLog().error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
