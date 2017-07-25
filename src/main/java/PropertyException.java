/**
 * Created by ielfaqir on 12/04/2017.
 */
public class PropertyException extends Exception {

    public static final String DIRECTORY_NOT_FOUND = "Directory not found";
    public static final String NOT_DIRECTORY = "Not a directory path";
    public static final String NOT_FILE = "Not a file path";
    public static final String NO_PROPERTIES_FILE_FOUND = "No file .properties found in origin";
    public static final String NULL_PATH = "Null path";
    public static final String NULL_PROPERTY_FILES = "Null object of property file";
    public static final String NO_SIZE_MATCH_PROPERTY_FILES = "The files on origin doesn't match those in destination";
    public static final String NO_PROPERTY_IN_FILES = "No Properties found in the file";
    private String message;

    public PropertyException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
