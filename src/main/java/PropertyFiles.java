import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ielfaqir on 12/04/2017.
 * Cette classe permet de récuperer tous les fichiers properties à partir d'un dossier
 */
public class PropertyFiles {

    private File directory;
    private List<File> properties;
    public static final String propertiesExtension = "properties";

    public PropertyFiles(String directory) throws PropertyException {

        if (directory == null || directory.equals(""))
            throw new PropertyException(PropertyException.NULL_PATH);

        this.directory = new File(directory);

        if (!this.directory.exists())
            throw new PropertyException(PropertyException.DIRECTORY_NOT_FOUND);

        if (!this.directory.isDirectory())
            throw new PropertyException(PropertyException.NOT_DIRECTORY);

        this.properties = new ArrayList<File>();
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public List<File> getProperties() throws PropertyException {

        if (!properties.isEmpty())
            return properties;

        else {
            this.findPropertyFiles(this.directory);
            return properties;
        }
    }

    public void setProperties(List<File> properties) {
        this.properties = properties;
    }

    private void findPropertyFiles(File currentDir) {

        this.properties = new ArrayList(FileUtils.listFiles(currentDir,
                new WildcardFileFilter("*." + propertiesExtension), DirectoryFileFilter.DIRECTORY));
    }
}
