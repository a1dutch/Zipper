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

import static uk.co.a1dutch.zipper.ArchiveUtils.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class offers a fluent api to decompress and unpackage a zip archive.
 * <p>
 * The following code shows how to unzip an archive:
 * </p>
 * 
 * <pre>
 * Unzipper.archive(myArchive).to(outputDirectory).unzip();
 * </pre>
 * 
 * @author a1dutch
 * @since 1.0.0
 */
public class Unzipper {

    private Logger log = LoggerFactory.getLogger(getClass());

    private File archive;
    private File outputDirectory;

    /**
     * Returns a new Unzipper for the given archive.
     * 
     * @param archive
     *            the archive to be unzipped.
     * 
     * @return a {@link Unzipper} used to collect all parameters.
     */
    public static Unzipper archive(String archive) {
        return archive(new File(archive));
    }

    /**
     * Returns a new Unzipper for the given archive.
     * 
     * @param archive
     *            the archive to be unzipped.
     * 
     * @return a {@link Unzipper} used to collect all parameters.
     */
    public static Unzipper archive(File archive) {
        return new Unzipper(archive);
    }

    private Unzipper(File archive) {
        this.archive = archive;
    }

    /**
     * Sets the directory where the archive should be unzipped too.
     * 
     * @param outputDirectory
     *            the directory where to unzip the archive too.
     * 
     * @return a {@link Unzipper} used to collect all parameters.
     */
    public Unzipper to(File outputDirectory) {
        this.outputDirectory = outputDirectory;
        return this;
    }

    /**
     * Sets the directory where the archive should be unzipped too.
     * 
     * @param outputDirectory
     *            the directory where to unzip the archive too.
     * 
     * @return a {@link Unzipper} used to collect all parameters.
     */
    public Unzipper to(String outputDirectory) {
        return to(new File(normalisePath(outputDirectory)));
    }

    /**
     * Unzips the archive to the given output directory.
     */
    public void unzip() {
        try {
            log.info("unzipping archive: {} into {}", archive, outputDirectory);
            try (ZipFile zip = new ZipFile(archive)) {
                for (Enumeration<? extends ZipEntry> elements = zip.entries(); elements.hasMoreElements();) {
                    ZipEntry entry = elements.nextElement();

                    String outputName = outputDirectory + "/" + entry.getName();
                    if (log.isDebugEnabled()) {
                        log.debug("unzipping entry: {}", entry.getName());
                    }

                    File output = new File(outputName);
                    if (entry.isDirectory()) {
                        output.mkdirs();
                        continue;
                    } else {
                        output.getParentFile().mkdirs();
                    }

                    try (BufferedInputStream bis = new BufferedInputStream(zip.getInputStream(entry));
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(output))) {
                        byte[] buffer = new byte[1024];
                        int read = 0;
                        while ((read = bis.read(buffer)) != -1) {
                            bos.write(buffer, 0, read);
                        }
                    }
                }
                log.info("unzipped {} entries", zip.size());
            }
        } catch (IOException e) {
            log.error("failed unzipping archive, {}", e.getMessage());
            throw new UnzipperRuntimeException("Failed to unzip archive", e);
        }
    }
}
