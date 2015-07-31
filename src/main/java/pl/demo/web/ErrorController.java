package pl.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.demo.web.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertsikora on 30.07.15.
 */

@Component
public class ErrorController {

    private final static Logger logger = LoggerFactory.getLogger(ErrorController.class);


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(final EmptyResultDataAccessException ex) {
        logger.error("General exception occurs", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> handleEmptyResultDataAccessException(final EmptyResultDataAccessException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(final ResourceNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    String handleException(final MethodArgumentNotValidException ex) {
        final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        final List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        final List<String> errors = new ArrayList<>(fieldErrors.size() + globalErrors.size());
        String error;
        for (final FieldError fieldError : fieldErrors) {
            error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
            errors.add(error);
        }
        for (final ObjectError objectError : globalErrors) {
            error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
            errors.add(error);
        }
        return errors.toString();
    }
}
