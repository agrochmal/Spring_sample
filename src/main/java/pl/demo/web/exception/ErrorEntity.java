package pl.demo.web.exception;

/**
 * Created by robertsikora on 01.08.15.
 */
public class ErrorEntity {

    private final String errorMsg;
    private final String developerMsg;

    public ErrorEntity(final String errorMsg, final String developerMsg) {
        this.errorMsg = errorMsg;
        this.developerMsg = developerMsg;
    }
}
