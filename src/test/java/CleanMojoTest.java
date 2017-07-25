import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by ielfaqir on 13/04/2017.
 */
public class CleanMojoTest extends AbstractMojoTestCase {

    private File pom;
    private final String path_destination = "src/test/resources/files/destination/";
    private File destination;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pom = getTestFile("src/test/resources/pom.xml");
        destination = new File(path_destination);
        if (!destination.exists() || !destination.isDirectory())
            throw new PropertyException(PropertyException.DIRECTORY_NOT_FOUND);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testCleanMojo() throws Exception {

        assertNotNull(pom);
        assertTrue(pom.exists());

        GenerateMojo generateMojo = (GenerateMojo) lookupMojo("generate", pom);
        assertNotNull(generateMojo);
        generateMojo.execute();

        Assert.assertNotNull(destination.listFiles());
        CleanMojo cleanMojo = (CleanMojo) lookupMojo("clean", pom);
        assertNotNull(cleanMojo);
        cleanMojo.execute();

        Assert.assertEquals(0, destination.listFiles().length);
    }

}
