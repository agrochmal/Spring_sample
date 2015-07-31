package pl.demo.web.exception;

/**
 * Created by robertsikora on 30.07.15.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        this("Resource not found!");
    }

    public ResourceNotFoundException(String message) {
        this(message, null);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
