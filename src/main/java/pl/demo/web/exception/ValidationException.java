package pl.demo.web.exception;


/**
 * Created by robertsikora on 30.07.15.
 */
public class ValidationException extends AbstractException {

    public ValidationException(final String message) {
        this(message, null);
    }

    public ValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
