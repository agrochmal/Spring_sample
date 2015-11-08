package pl.demo.core.service.registration;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import pl.demo.core.model.entity.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by robertsikora on 05.11.2015.
 */

@Validated
public interface RegistrationService {

    User registerAccount(@NotNull @Valid User user);

    void activateAccount(Long userId, String activationCode);

    @PreAuthorize("hasRole('ADMIN')")
    void deactivateAccount(Long userId);
}
