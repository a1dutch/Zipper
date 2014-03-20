/*
 * Copyright 2014 Andrew Holland.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.a1dutch.zipper;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ZipperTest extends AbstractArchiverTest {

    private static final String OUTPUT_DIR = TEST_DATA_DIR + "/zipper";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldThrowZipperRuntimeExceptionWhenArchiveNotZip() throws Exception {
        exception.expect(ZipperRuntimeException.class);
        exception.expectMessage(equalTo("archive must have a .zip extension"));

        Zipper.archive("a.notazip");
    }

    @Test
    public void shouldThrowZipperRuntimeExceptionWhenArchiveExists() throws Exception {
        String file = outputZip("exists");

        File zip = new File(file);
        zip.getParentFile().mkdirs();
        zip.createNewFile();

        exception.expect(ZipperRuntimeException.class);
        exception.expectMessage(equalTo("archive already exists"));

        Zipper.archive(file).zip();
    }

    @Test
    public void shouldThrowZipperRuntimeExceptionWhenArchiveNull() throws Exception {
        exception.expect(ZipperRuntimeException.class);
        exception.expectMessage(equalTo("archive must not be null"));

        Zipper.archive((String) null);
    }

    @Test
    public void shouldThrowZipperRuntimeExceptionWhenArchiveCannotBeCreated() throws Exception {
        exception.expect(ZipperRuntimeException.class);
        exception.expectMessage(equalTo("failed to create archive"));

        File mock = mock(File.class);
        when(mock.exists()).thenReturn(false);
        when(mock.getName()).thenReturn("archive.zip");
        when(mock.getParentFile()).thenReturn(mock(File.class));
        when(mock.createNewFile()).thenThrow(new IOException());

        Zipper.archive(mock).zip();
    }

    @Test
    public void shouldCreateArchiveGivenDirectory() throws Exception {
        String FILE_NAME = "archiveFromDirectoryName";

        Zipper.archive(outputZip(FILE_NAME)).directory(ARCHIVES_DIR + "/a").zip();

        assertZip(FILE_NAME, 3);
        assertZipContent(FILE_NAME, "a/a/a.txt", "a.txt");
        assertZipContent(FILE_NAME, "a/a.txt", "a.txt");
        assertZipContent(FILE_NAME, "a/b.txt", "b.txt");
    }

    @Test
    public void shouldCreateArchiveGivenFile() throws Exception {
        String FILE_NAME = "archiveFromDirectoryName";

        Zipper.archive(outputZip(FILE_NAME)).file(newArchive("/a/a.txt")).zip();

        assertZip(FILE_NAME, 1);
        assertZipContent(FILE_NAME, "a.txt");
    }

    private void assertZip(String file, int entries) throws IOException {
        try (ZipFile zip = new ZipFile(outputZip(file))) {
            assertThat(zip.size(), is(equalTo(entries)));
        }
    }

    private void assertZipContent(String file, String zipPath) throws IOException {
        assertZipContent(file, zipPath, zipPath);
    }

    private void assertZipContent(String file, String zipPath, String fileContent) throws IOException {
        try (ZipFile zip = new ZipFile(outputZip(file))) {
            ZipEntry entry = zip.getEntry(zipPath);
            assertThat(entry, is(notNullValue()));

            try (InputStream zeis = zip.getInputStream(entry)) {
                byte[] buffer = new byte[(int) entry.getSize()];
                IOUtils.read(zeis, buffer);

                assertThat(new String(buffer), is(equalTo(fileContent)));
            }
        }
    }

    private String newArchive(String file) {
        return ARCHIVES_DIR + file;
    }

    private String outputZip(String OUTPUT_FILE) {
        return OUTPUT_DIR + "/" + OUTPUT_FILE + ".zip";
    }
}
