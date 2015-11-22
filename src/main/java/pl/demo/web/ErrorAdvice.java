package pl.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.demo.web.exception.ErrorInfo;
import pl.demo.web.exception.ResourceNotFoundException;
import pl.demo.web.exception.ServerException;
import pl.demo.web.exception.ValidationException;


/**
 * Created by robertsikora on 30.07.15.
 */

@ControllerAdvice
public class ErrorAdvice {

    private final static Logger LOGGER = LoggerFactory.getLogger(ErrorAdvice.class);

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationRequestException(final ValidationException ex) {
        LOGGER.error("Validation exception occurs", ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<?> handleResourceNotFoundException(final ResourceNotFoundException ex) {
        LOGGER.error("Resource not found exception occurs", ex);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<?> handleUsernameNotFoundException(final UsernameNotFoundException ex) {
        LOGGER.error("Resource not found exception occurs", ex);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({ServerException.class})
    public ResponseEntity<?> handleGeneralException(final ServerException ex) {
        LOGGER.error("General exception occurs", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(new ErrorInfo("Fatal error on server", ex.getMessage()));
    }
}
