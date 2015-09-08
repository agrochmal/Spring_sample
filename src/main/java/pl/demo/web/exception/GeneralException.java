package pl.demo.web.exception;


/**
 * Created by robertsikora on 30.07.15.
 */
public class GeneralException extends AbstractException {

    public GeneralException(final String message) {
        this(message, null);
    }

    public GeneralException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
