package uk.co.a1dutch.zipper;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import uk.co.a1dutch.zipper.Unzipper;

/**
 * @author a1dutch
 */
public class UnzipperTest extends AbstractArchiverTest {

    private static final String OUTPUT_DIR = TEST_DATA_DIR + "/unzipper";
    
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldUnzipArchive() throws Exception {
        Unzipper.archive(ARCHIVES_DIR + "/a.zip").to(OUTPUT_DIR + "/a").unzip();
        assertThat(new File(OUTPUT_DIR + "/a").exists(), is(equalTo(true)));
        assertThat(FileUtils.readFileToString(new File(OUTPUT_DIR + "/a/a/a.txt")), is(equalTo("a.txt")));
        assertThat(FileUtils.readFileToString(new File(OUTPUT_DIR + "/a/a.txt")), is(equalTo("a.txt")));
        assertThat(FileUtils.readFileToString(new File(OUTPUT_DIR + "/a/b.txt")), is(equalTo("b.txt")));
    }

    @Test
    public void shouldThrowRuntimeErrorWhenUnzipBadArchive() throws Exception {
        exception.expect(UnzipperRuntimeException.class);
        exception.expectMessage(equalTo("Failed to unzip archive"));
        
        Unzipper.archive(ARCHIVES_DIR + "/faulty.zip").to(OUTPUT_DIR + "/b").unzip();
    }

    @Test
    public void shouldThrowRuntimeErrorWhenUnzipNoneExistentArchive() throws Exception {
        exception.expect(UnzipperRuntimeException.class);
        exception.expectMessage(equalTo("Failed to unzip archive"));
        
        Unzipper.archive(ARCHIVES_DIR + "/z.zip").to(OUTPUT_DIR + "/z").unzip();
    }
}
