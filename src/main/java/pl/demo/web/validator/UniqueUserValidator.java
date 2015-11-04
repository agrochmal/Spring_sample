package pl.demo.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import pl.demo.core.service.user.UserService;
import pl.demo.web.exception.ResourceNotFoundException;

/**
 * Created by robertsikora on 08.10.15.
 */

@Component
public class UniqueUserValidator implements BusinessValidator {

    private UserService userService;

    @Override
    public boolean validate(Object target) {
        Assert.hasText((String)target);
        try {
            this.userService.loadUserByUsername((String)target);
        }catch (final ResourceNotFoundException ex){
            return true;
        }
        return false;
    }

    @Autowired
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }
}
