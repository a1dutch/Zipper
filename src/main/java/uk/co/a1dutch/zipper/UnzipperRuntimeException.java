package uk.co.a1dutch.zipper;

/**
 * Exception to be thrown when unzipping an archive.
 * 
 * @author a1dutch
 */
@SuppressWarnings("serial")
public class UnzipperRuntimeException extends RuntimeException {
    public UnzipperRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
