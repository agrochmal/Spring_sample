package pl.demo.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.demo.core.service.UserService;
import pl.demo.web.exception.ResourceNotFoundException;

/**
 * Created by robertsikora on 08.10.15.
 */

@Component
public class UniqueUserValidator implements CustomValidator {

    private UserService userService;

    @Override
    public boolean validate(Object target) {
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
