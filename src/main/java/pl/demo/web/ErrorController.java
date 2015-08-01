package pl.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.demo.web.exception.ErrorEntity;
import pl.demo.web.exception.ResourceNotFoundException;
import pl.demo.web.exception.ValidationRequestException;


/**
 * Created by robertsikora on 30.07.15.
 */

@ControllerAdvice
public class ErrorController {

    private final static Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(ValidationRequestException.class)
    public ResponseEntity<?> handleValidationRequestException(final ValidationRequestException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(final ResourceNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(final Exception ex) {
        logger.error("General exception occurs", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(new ErrorEntity("Fatal error on server", ex.getMessage()));
    }
}
