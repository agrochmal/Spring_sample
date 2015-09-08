package pl.demo.web.exception;

import pl.demo.MsgConst;

/**
 * Created by robertsikora on 30.07.15.
 */
public class ValidationRequestException extends AbstractException {

    public ValidationRequestException() {
        this(MsgConst.VALIDATION_FAILED);
    }

    public ValidationRequestException(final String message) {
        this(message, null);
    }

    public ValidationRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
