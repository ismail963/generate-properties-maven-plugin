import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by ielfaqir on 13/04/2017.
 */
public class GenerateMojoTest extends AbstractMojoTestCase {

    private File pom;
    private GenerateMojo myMojo;
    private final String path_origin = "src/test/resources/files/origin/";
    private final String path_destination = "src/test/resources/files/destination/";
    private final String filename_test = "test." + PropertyFiles.propertiesExtension;
    private File origin, destination;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pom = getTestFile("src/test/resources/pom.xml");
        myMojo = (GenerateMojo) lookupMojo("generate", pom);
        destination = new File(path_destination);
        origin = new File(path_origin);

        this.clean();

        if (!origin.exists() || !origin.isDirectory() || !destination.exists() || !destination.isDirectory())
            throw new PropertyException(PropertyException.DIRECTORY_NOT_FOUND);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.clean();
    }

    @Before
    public void clean() throws Exception {

        if (destination != null) {
            for (File file : destination.listFiles()) {
                file.delete();
            }
        }
        FileUtils.getFile(path_origin + filename_test).delete();
        FileUtils.getFile(path_destination + filename_test).deleteOnExit();
    }

    @Test
    public void testCompareMojo() throws Exception {

        assertNotNull(pom);
        assertTrue(pom.exists());
        assertNotNull(myMojo);
        myMojo.execute();
    }

    @Test(expected = PropertyException.class)
    public void testDestinationHasMoreFileThanOrigin() throws Exception {

        for (File file : origin.listFiles()) {
            new FileOutputStream(path_destination + file.getName(), false).close();
        }
        new FileOutputStream(path_destination + filename_test, false).close();

        myMojo.execute();
    }

    @Test(expected = PropertyException.class)
    public void testOriginHasFileWithoutPropriety() throws Exception {

        myMojo.execute();
        Assert.assertEquals(origin.listFiles().length, destination.listFiles().length);

        new FileOutputStream(path_origin + filename_test, false).close();

        myMojo.execute();
        Assert.assertNotEquals(destination.listFiles().length, origin.listFiles().length);
    }

    @Test
    public void testOriginHasMoreFileThanDestination() throws Exception {

        myMojo.execute();
        Assert.assertEquals(origin.listFiles().length, destination.listFiles().length);

        FileUtils.copyFile(origin.listFiles()[0], new File(path_origin + filename_test));

        Assert.assertEquals(destination.listFiles().length + 1, origin.listFiles().length);

        myMojo.execute();
        Assert.assertEquals(destination.listFiles().length, origin.listFiles().length);
    }

}
