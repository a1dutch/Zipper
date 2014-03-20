package uk.co.a1dutch.zipper;

/**
 * Utility class with common archive functions.
 *  
 * @author a1dutch 
 * @since 1.1.0
 */
final class ArchiveUtils {
    /**
     * Normalises a path by replacing all back slashes with forward slashes.
     * 
     * @param path the path to normalise.
     * @return the normalised path.
     */
    static String normalisePath(String path) {
        return path.replace('\\', '/');
    }
}
