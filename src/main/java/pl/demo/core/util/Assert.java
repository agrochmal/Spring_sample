package pl.demo.core.util;

import org.springframework.validation.BindingResult;
import pl.demo.web.exception.ResourceNotFoundException;
import pl.demo.web.exception.ValidationException;

/**
 * Created by robertsikora on 08.10.15.
 */
public final class Assert extends org.springframework.util.Assert {

    /**
     * Assert that an object is not {@code null} .
     * @param object the object to check
     * @throws ResourceNotFoundException if the object is {@code null}
     */
    public static void notResourceFound(final Object object) {
        if (object == null) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Assert that an object is not {@code null} .
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws ResourceNotFoundException if the object is {@code null}
     */
    public static void notResourceFound(final Object object, final String message) {
        if (object == null) {
            throw new ResourceNotFoundException(message);
        }
    }

    /**
     * Assert that an object is not {@code null} .
     * @param bindingResult the object to check
     * @throws ValidationException if the object has errors
     */
    public static void hasErrors(final BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new ValidationException(resolveErrorMessage(bindingResult));
        }
    }

    /**
     * Assert that an object is not created .
     * @throws AssertionError always
     */
    public static void noObject(){
        throw new AssertionError("Cannot create object");
    }


    private static String resolveErrorMessage(final BindingResult bindingResult){
        Assert.notNull(bindingResult);
        final StringBuilder str = new StringBuilder();
        bindingResult.getFieldErrors().forEach(
                t ->  str.append(t.getDefaultMessage()).append(Utils.LINE_SEPARATOR)
        );
        return str.toString();
    }

}
