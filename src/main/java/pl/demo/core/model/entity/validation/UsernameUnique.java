package pl.demo.core.model.entity.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by robertsikora on 07.12.2015.
 */
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {UsernameUniqueValidator.class})
public @interface UsernameUnique {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };
}
