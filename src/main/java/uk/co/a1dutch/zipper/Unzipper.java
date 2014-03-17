package uk.co.a1dutch.zipper;

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
 * This class offers a fluent api to decompress and unpackage zip archive.
 * <p>
 * The following code shows how to unzip an archive:
 * </p>
 * 
 * <pre>
 * Unzipper.archive(myArchive).to(outputDirectory).unzip();
 * </pre>
 */
public class Unzipper {

    private Logger log = LoggerFactory.getLogger(getClass());

    private File archive;
    private File outputDirectory;

    /**
     * Returns a new Unzipper for the given archive.
     * 
     * @param archive the archive to be unzipped.
     * 
     * @return a {@link Unzipper} used to collect all parameters.
     */
    public static Unzipper archive(String archive) {
        return archive(new File(archive));
    }

    /**
     * Returns a new Unzipper for the given archive.
     * 
     * @param archive the archive to be unzipped.
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
     * @param outputDirectory the directory where to unzip the archive too.
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
     * @param outputDirectory the directory where to unzip the archive too.
     * 
     * @return a {@link Unzipper} used to collect all parameters.
     */
    public Unzipper to(String outputDirectory) {
        return to(new File(normalise(outputDirectory)));
    }

    /**
     * Unzips the archive to the given output directory.
     */
    public void unzip() {
        try {
            log.info("unzipping archive: " + archive + " into " + outputDirectory);
            ZipFile zip = new ZipFile(archive);
            for (Enumeration<? extends ZipEntry> elements = zip.entries(); elements.hasMoreElements();) {
                ZipEntry entry = elements.nextElement();

                String outputName = outputDirectory + "/" + entry.getName();
                log.debug("unzipping entry: " + entry.getName());

                if (entry.isDirectory()) {
                    new File(outputName).mkdirs();
                    continue;
                }

                BufferedInputStream bis = new BufferedInputStream(zip.getInputStream(entry));
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(outputName)));
                byte[] buffer = new byte[1024];
                int read = 0;
                while ((read = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, read);
                }
                bis.close();
                bos.close();
            }
            log.info("unzipped " + zip.size() + " entries.");
            zip.close();
        } catch (IOException e) {
            log.error("failed unzipping archive, " + e.getMessage());
            throw new RuntimeException("Failed to unzip archive, " + e.getMessage());
        }
    }

    private String normalise(String outputDirectory) {
        return outputDirectory.replaceAll("\\\\", "/");
    }
}