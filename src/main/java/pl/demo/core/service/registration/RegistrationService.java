package pl.demo.core.service.registration;

import org.springframework.validation.annotation.Validated;
import pl.demo.core.model.entity.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by robertsikora on 05.11.2015.
 */

@Validated
public interface RegistrationService {

    User register(@NotNull @Valid User user);
}
