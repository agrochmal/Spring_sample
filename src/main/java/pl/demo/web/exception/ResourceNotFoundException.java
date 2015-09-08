package pl.demo.web.exception;

import pl.demo.MsgConst;

/**
 * Created by robertsikora on 30.07.15.
 */
public class ResourceNotFoundException extends AbstractException {

    public ResourceNotFoundException() {
        this(MsgConst.RESOURCE_NOT_FOUND);
    }

    public ResourceNotFoundException(final String message) {
        this(message, null);
    }

    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
