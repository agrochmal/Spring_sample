package pl.demo.core.model.entity.validation;

import org.springframework.beans.factory.annotation.Autowired;
import pl.demo.core.model.repo.UserRepository;
import pl.demo.core.service.registration.AccountStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by robertsikora on 07.12.2015.
 */

public class UsernameUniqueValidator implements ConstraintValidator<UsernameUnique, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(final UsernameUnique constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return userRepository == null || this.userRepository.findByUsername(value, AccountStatus.ACTIVE) == null;
    }
}
