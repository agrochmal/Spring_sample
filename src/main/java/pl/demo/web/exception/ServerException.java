package pl.demo.web.exception;


/**
 * Created by robertsikora on 30.07.15.
 */
public class ServerException extends AbstractException {

    public ServerException(final String message) {
        this(message, null);
    }

    public ServerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
