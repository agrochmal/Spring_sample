package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.demo.core.model.entity.User;
import pl.demo.core.service.registration.RegistrationService;
import pl.demo.core.service.validator.BusinessValidator;
import pl.demo.core.util.Assert;
import pl.demo.core.util.WebUtils;

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

        return Optional.ofNullable(registrationService.registerAccount(user))
                .map(t -> ResponseEntity.created(WebUtils.createURI(t.getId())).build())
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Override
    public ResponseEntity<?> activateAccount(@PathVariable("id") final Long userId, final String activationCode) {
        registrationService.activateAccount(userId, activationCode);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> deactivateAccount(@PathVariable("id") final Long userId) {
        registrationService.deactivateAccount(userId);
        return ResponseEntity.ok().build();
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
