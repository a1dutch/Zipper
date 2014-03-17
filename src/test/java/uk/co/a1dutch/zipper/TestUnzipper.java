package uk.co.a1dutch.zipper;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import uk.co.a1dutch.zipper.Unzipper;

public class TestUnzipper {
    
    @Before
    public void setup() {
        FileUtils.deleteQuietly(new File("target/output/a"));
    }
    
    @Test
    public void shouldUnzipArchive() throws Exception {
        Unzipper.archive("target/test-classes/archives/a.zip").to("target/output/a").unzip();
        assertThat(new File("target/output/a").exists(), is(equalTo(true)));
        assertThat(FileUtils.readFileToString(new File("target/output/a/a/a.txt")), is(equalTo("a.txt")));
        assertThat(FileUtils.readFileToString(new File("target/output/a/a.txt")), is(equalTo("a.txt")));
        assertThat(FileUtils.readFileToString(new File("target/output/a/b.txt")), is(equalTo("b.txt")));
    }
    
    @Test(expected=RuntimeException.class)
    public void shouldThrowRuntimeErrorWhenUnzipBadArchive() throws Exception {
        Unzipper.archive("target/test-classes/archives/b.zip").to("target/output/b").unzip();
    }
    
    @Test(expected=RuntimeException.class)
    public void shouldThrowRuntimeErrorWhenUnzipNoneExistentArchive() throws Exception {
        Unzipper.archive("target/test-classes/archives/z.zip").to("target/output/z").unzip();
    }
}
