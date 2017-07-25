import org.apache.maven.plugin.logging.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ielfaqir on 12/04/2017.
 */
public class CreateProperties {

    private static boolean enableComment = true;

    private static String comment = "#";

    private static String separator = "=";

    private static String openValueSeparator = "{{ ";

    private static String closeValueSeparator = " }}";

    public static enum LineType {
        Propriety, Comment
    }

    private File origin;
    private File destinationDirectory;

    private Map<String, LineType> origin_proprieties;

    private Log logger;

    /*
    * @bref : Permet de réinitialiser les constantes par les configuration demandé
    * */
    public static void init(boolean enableComment, String comment, String separator,
                            String openValueSeparator, String closeValueSeparator) {

        CreateProperties.enableComment = enableComment;

        if (comment != null)
            CreateProperties.comment = comment;
        if (separator != null)
            CreateProperties.separator = separator;
        if (openValueSeparator != null)
            CreateProperties.openValueSeparator = openValueSeparator;
        if (closeValueSeparator != null)
            CreateProperties.closeValueSeparator = closeValueSeparator;
    }

    public CreateProperties(File origin, File destination, Log logger) throws PropertyException {

        if (!origin.isFile())
            throw new PropertyException(PropertyException.NOT_FILE);
        if (!destination.isDirectory())
            throw new PropertyException(PropertyException.NOT_DIRECTORY);

        this.origin = origin;
        this.destinationDirectory = destination;
        this.logger = logger;

        this.readOriginFile();
        this.createFileDestination();
    }

    /*
    * @bref : Permet de crée un fichier proprieties sur la destination
    *        à partir des proprieties sur le fichier d'origin
    * */
    public void createFileDestination() throws PropertyException {
        if (this.origin_proprieties.isEmpty())
            throw new PropertyException(PropertyException.NO_PROPERTY_IN_FILES);

        logger.info("Trying to create file " + origin.getName() + " for destination");

        try {
            final String filename = destinationDirectory.getAbsolutePath() + "/" + origin.getName();

            // create an empty file whether it exists or not
            new FileOutputStream(filename, false).close();

            // write in file
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            for (String line : this.origin_proprieties.keySet()) {
                if (this.origin_proprieties.get(line) == LineType.Comment)
                    writer.println(line);
                else
                    writer.println(line + separator + openValueSeparator + line + closeValueSeparator);
            }
            writer.close();
            logger.info("File " + origin.getName() + " created on destination");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * @bref : Permet de récuperer les proprieties du fichier d'origin
    * cette méthode lévera une exception si le fichier est vide.
    * */
    private void readOriginFile() throws PropertyException {

        this.origin_proprieties = new HashMap<String, LineType>();

        try {
            logger.info("Trying to read file " + origin.getName());
            BufferedReader br = new BufferedReader(new FileReader(this.origin));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.indexOf(comment) == -1) {
                    String[] value = line.split(separator);
                    if (value.length == 2) {
                        origin_proprieties.put(value[0], LineType.Propriety);
                    }
                } else if (enableComment) {
                    origin_proprieties.put(line, LineType.Comment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
