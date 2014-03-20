package uk.co.a1dutch.zipper;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;

/**
 * @author a1dutch
 */
public abstract class AbstractArchiverTest {
    
    protected static final String TEST_DATA_DIR = "target/test-data";

    protected static final String ARCHIVES_DIR = "target/test-classes/archives";

    @Before
    public void deleteTestDataDirectory() throws IOException {
        FileUtils.deleteDirectory(new File(TEST_DATA_DIR));
    }
}
