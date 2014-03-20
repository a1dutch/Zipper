package uk.co.a1dutch.zipper;

import static org.apache.commons.io.FileUtils.*;
import static org.apache.commons.io.filefilter.FileFilterUtils.*;
import static uk.co.a1dutch.zipper.ArchiveUtils.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class offers a fluent api to compress and package a zip archive. <br>
 * <br>
 * zip an archive with a directory:
 * 
 * <pre>
 * <code>Zipper.archive(myArchive).directory(directoryToZip).zip();</code>
 * </pre>
 * 
 * zip an archive with a file:
 * 
 * <pre>
 * <code>Zipper.archive(myArchive).file(fileToZip).zip()}</code>
 * </pre>
 * 
 * </p>
 * 
 * @author a1dutch
 * @since 1.1.0
 */
public class Zipper {
    /**
     * Creates a new Zipper for the given archive.
     * 
     * @param archive
     *            the archive to be created.
     * @return a {@link Zipper} used to collect all parameters.
     */
    public static Zipper archive(File archive) {
        return new Zipper(archive);
    }

    /**
     * Creates a new Zipper for the given archive.
     * 
     * @param archive
     *            the archive to be created.
     * @return a {@link Zipper} used to collect all parameters.
     */
    public static Zipper archive(String archive) {
        return archive(archive == null ? null : new File(archive));
    }

    private Logger log = LoggerFactory.getLogger(getClass());

    private List<File> files = new ArrayList<>();
    private Map<File, String> paths = new HashMap<>();
    private File archive;

    private Zipper(File archive) {
        this.archive = archive;

        if (archive == null) {
            throw new ZipperRuntimeException(new IllegalArgumentException("archive must not be null"));
        }

        if (!archive.getName().endsWith(".zip")) {
            throw new ZipperRuntimeException("archive must have a .zip extension");
        }
    }

    /**
     * Adds the directories to be added to the generated archive.
     * 
     * @param directory
     *            the directory to add to the archive.
     * 
     * @return a {@link Zipper} used to collect all parameters.
     */
    public Zipper directory(File directory) {
        int directoryPathLength = directory.getPath().length() - directory.getName().length();
        for (File file : filesInDirectory(directory)) {
            files.add(file);
            paths.put(file, normalisePath(file.getPath().substring(directoryPathLength)));
        }
        return this;
    }

    /**
     * Adds a directory to be added to the generated archive.
     * 
     * @param directory
     *            the directory to add to the archive.
     * 
     * @return a {@link Zipper} used to collect all parameters.
     */
    public Zipper directory(String directory) {
        return directory(new File(directory));
    }

    /**
     * Adds the directories to the generated archive.
     * 
     * @param directories
     *            the directories to be added to the archive.
     * 
     * @return a {@link Zipper} used to collect all parameters.
     */
    public Zipper directories(File... directories) {
        for (File directory : directories) {
            directory(directory);
        }
        return this;
    }

    /**
     * Adds the directories to be added to the generated archive.
     * 
     * @param directories
     *            the directories to be added to the archive.
     * 
     * @return a {@link Zipper} used to collect all parameters.
     */
    public Zipper directories(String... directories) {
        for (String directory : directories) {
            directory(directory);
        }
        return this;
    }

    /**
     * Add a file to be added to the generated archive.
     * 
     * @param file
     *            the file to add to the archive.
     * 
     * @return a {@link Zipper} used to collect all parameters.
     */
    public Zipper file(File file) {
        return files(file);
    }

    /**
     * Add a file to be added to the generated archive.
     * 
     * @param file
     *            the file to add to the archive.
     * 
     * @return a {@link Zipper} used to collect all parameters.
     */
    public Zipper file(String file) {
        return files(file);
    }

    /**
     * Adds the files to be added to the generated archive.
     * 
     * @param files
     *            the files to be added to the archive.
     * 
     * @return a {@link Zipper} used to collect all parameters.
     */
    public Zipper files(File... files) {
        for (File f : files) {
            this.files.add(f);
        }
        return this;
    }

    /**
     * Adds the files to be added to the generated archive.
     * 
     * @param files
     *            the files to be added to the archive.
     * 
     * @return a {@link Zipper} used to collect all parameters.
     */
    public Zipper files(String... files) {
        for (String f : files) {
            files(new File(f));
        }
        return this;
    }

    /**
     * Creates the archive.
     */
    public void zip() {
        log.info("zipping archive: {}", archive);

        if (archive.exists()) {
            log.error("failed to create archive: archive already exists");
            throw new ZipperRuntimeException("archive already exists");
        }

        try {
            archive.getParentFile().mkdirs();
            archive.createNewFile();
        } catch (IOException e) {
            log.error("failed to create archive: ", e.getMessage());
            throw new ZipperRuntimeException("failed to create archive", e);
        }

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(archive))) {
            for (File file : files) {
                String entryName = paths.get(file) == null ? file.getName() : paths.get(file);
                zos.putNextEntry(new ZipEntry(entryName));

                if (log.isDebugEnabled()) {
                    log.debug("zipping entry: {}", entryName);
                }

                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] b = new byte[1024];
                    int len = -1;
                    while ((len = fis.read(b)) != -1) {
                        zos.write(b, 0, len);
                    }
                    zos.closeEntry();
                }
            }
            zos.close();
            log.info("zipped {} entries", files.size());
        } catch (IOException e) {
            throw new ZipperRuntimeException("failed to write archive entries", e);
        }
    }

    private Collection<File> filesInDirectory(File directory) {
        return listFiles(directory, trueFileFilter(), trueFileFilter());
    }
}
