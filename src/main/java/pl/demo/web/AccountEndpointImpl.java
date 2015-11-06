package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.demo.core.model.entity.User;
import pl.demo.core.service.registration.RegistrationService;
import pl.demo.core.service.validator.BusinessValidator;
import pl.demo.core.util.Assert;
import pl.demo.core.util.Utils;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Created by robertsikora on 05.11.2015.
 */

@RestController
public class AccountEndpointImpl implements AccountEndpoint {

    private BusinessValidator validator;
    private RegistrationService registrationService;

    @Override
    public ResponseEntity<?> registerAccount(@Valid @RequestBody final User user, final BindingResult bindingResult) {
        Assert.hasErrors(bindingResult);

        return Optional.ofNullable(registrationService.register(user))
                .map(t -> ResponseEntity.created(Utils.createURI(t.getId())).build())
                .orElseGet(() -> ResponseEntity.noContent().build());
    }


    @Override
    public ResponseEntity<?> activateAccount(String activationCode) {
        throw new UnsupportedOperationException("Not implemented yet !");
    }

    @Override
    public ResponseEntity<Boolean> checkUniqueAccount(final String username) {
        return ResponseEntity.ok().body(validator.validate(username));
    }

    @Autowired
    @Qualifier("existUniqueUserValidator")
    public void setValidator(final BusinessValidator validator) {
        this.validator = validator;
    }

    @Autowired
    public void setRegistrationService(final RegistrationService registrationService) {
        this.registrationService = registrationService;
    }
}
