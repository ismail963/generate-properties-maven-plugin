import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.util.Arrays;

/**
 * Created by ielfaqir on 12/04/2017.
 * Cette classe permet de comparer les fichiers d'orgine et celles du destination
 */
public class CompareFiles {

    private PropertyFiles origin;
    private PropertyFiles destination;
    private Log logger;

    public CompareFiles(PropertyFiles origin, PropertyFiles destination, Log logger)
            throws PropertyException {

        if (origin == null || destination == null)
            throw new PropertyException(PropertyException.NULL_PROPERTY_FILES);

        if (origin.getProperties().isEmpty())
            throw new PropertyException(PropertyException.NO_PROPERTIES_FILE_FOUND);

        this.origin = origin;
        this.destination = destination;
        this.logger = logger;
    }

    public PropertyFiles getOrigin() {
        return origin;
    }

    public PropertyFiles getDestination() {
        return destination;
    }

    /*
    * @bref : Comparer les fichiers d'orgine avec la distination
    * cette méthode léve une exception si le nombre des fichiers sur la destination est plus que le nombre d'origin
    * */
    public void filesMatch() throws PropertyException {

        String names = "";
        if (origin.getProperties().size() != destination.getProperties().size()) {

            logger.warn("Files of properties don't match");

            if (origin.getProperties().size() > destination.getProperties().size()) {
                names = this.filesNotMatch(origin, destination);
                logger.warn("Origin has more files than destination [ " + names + "]");
            } else {
                names = this.filesNotMatch(destination, origin);
                logger.error("destination has more files than origin [ " + names + "]");
                throw new PropertyException(PropertyException.NO_SIZE_MATCH_PROPERTY_FILES);
            }
        } else {
            names = this.filesNotMatch(origin, destination);
            if (!names.equals("")) {
                this.dump();
                throw new PropertyException(PropertyException.NO_SIZE_MATCH_PROPERTY_FILES);
            }
            logger.info("Files of properties match");
        }

    }

    /*
    * @bref : Permet de crée les fichiers proprieties sur la destination
    * */
    public void create() throws PropertyException {
        File dest;
        if (!destination.getProperties().isEmpty()) {
            dest = destination.getProperties().get(0).getParentFile();
        } else {
            dest = destination.getDirectory();
        }

        for (File file : origin.getProperties()) {
            new CreateProperties(file, dest, logger);
        }
    }

    /*
    * @bref : Permet de récuperer les nom des fichiers non existants sur le 2eme repertoire
    * @param first : le premier répertoire
    * @param second : le 2eme répertoire
    * @return : String des nom des fichiers sur first et non existants sur second
    * */
    private String filesNotMatch(PropertyFiles first, PropertyFiles second) throws PropertyException {
        String filenames = "";
        for (File org : first.getProperties()) {
            Boolean check = false;
            for (File dest : second.getProperties()) {
                if (org.getName().equals(dest.getName())) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                if (!filenames.equals(""))
                    filenames += ", ";

                filenames += org.getName();
            }
        }
        return filenames;
    }

    /*
     * @bref : Permet d'afficher tous les fichiers d'origines et de destination sur la log
    * */
    private void dump() throws PropertyException {

        int size = origin.getProperties().size();
        File[] org = origin.getProperties().toArray(new File[size]);
        File[] dest = destination.getProperties().toArray(new File[size]);

        Arrays.sort(org);
        Arrays.sort(dest);

        final int n = 50;
        final String s = " ";
        logger.warn("Origin" + String.format("%0" + (n - 6) + "d", 0).replace("0", s) +
                "| destination" + String.format("%0" + (n - 13) + "d", 0).replace("0", s));
        for (int i = 0; i < org.length; i++) {
            logger.warn(org[i].getName() +
                    String.format("%0" + (n - org[i].getName().length()) + "d", 0).replace("0", s) +
                    "| " +
                    dest[i].getName() + String.format("%0" + (n - 2 - dest[i].getName().length()) + "d", 0).replace("0", s));
        }
    }
}
