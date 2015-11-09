package pl.demo.core.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import pl.demo.core.model.repo.UserRepository;
import pl.demo.core.service.registration.AccountStatus;
import pl.demo.web.exception.ResourceNotFoundException;

/**
 * Created by robertsikora on 08.10.15.
 */

@Component
public class ExistUniqueUserValidator implements BusinessValidator {

    private UserRepository userRepository;

    @Override
    public boolean validate(Object target) {
        Assert.hasText((String)target);
        try {
            return null != this.userRepository.findByUsername((String)target, AccountStatus.ACTIVE);
        }catch (final ResourceNotFoundException ex){
            return true;
        }
    }

    @Autowired
    public void setUserRepository(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
