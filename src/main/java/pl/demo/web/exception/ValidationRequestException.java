package pl.demo.web.exception;

/**
 * Created by robertsikora on 30.07.15.
 */
public class ValidationRequestException extends RuntimeException {

    public ValidationRequestException() {
        this("Validation failed!");
    }

    public ValidationRequestException(String message) {
        this(message, null);
    }

    public ValidationRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
