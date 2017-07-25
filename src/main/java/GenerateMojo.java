import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Created by ielfaqir on 12/04/2017.
 */
@Mojo(name = "generate")
public class GenerateMojo extends AbstractMojo {

    @Parameter(property = "generate.origin", required = true)
    private String origin;

    @Parameter(property = "generate.destination", required = true)
    private String destination;

    @Parameter(property = "generate.enableComment", defaultValue = "true")
    private boolean enableComment;

    @Parameter(property = "generate.comment", defaultValue = "#")
    private String comment;

    @Parameter(property = "generate.separator", defaultValue = "=")
    private String separator;

    @Parameter(property = "generate.openValueSeparator", defaultValue = "{{ ")
    private String openValueSeparator;

    @Parameter(property = "generate.closeValueSeparator", defaultValue = " }}")
    private String closeValueSeparator;


    public void execute() throws MojoExecutionException {
        try {

            getLog().info("Init parameters");
            CreateProperties.init(enableComment, comment, separator, openValueSeparator, closeValueSeparator);

            getLog().info("Origin ==> " + origin);
            PropertyFiles org = new PropertyFiles(origin);
            getLog().info("Origin propriety files found : " + org.getProperties().size());

            getLog().info("Destination ==> " + destination);
            PropertyFiles dest = new PropertyFiles(destination);
            getLog().info("Destination propriety files found : " + dest.getProperties().size());

            CompareFiles generateFiles = new CompareFiles(org, dest, getLog());
            generateFiles.filesMatch();
            generateFiles.create();

        } catch (PropertyException e) {
            getLog().error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
