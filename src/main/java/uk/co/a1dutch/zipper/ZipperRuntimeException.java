package uk.co.a1dutch.zipper;

/**
 * Exception to be thrown when zipping an archive.
 * 
 * @author a1dutch
 * @since 1.1.0
 */
@SuppressWarnings("serial")
public class ZipperRuntimeException extends RuntimeException {
    public ZipperRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ZipperRuntimeException(String message) {
        super(message);
    }

    public ZipperRuntimeException(Throwable throwable) {
        super(throwable.getMessage(), throwable);
    }
}
