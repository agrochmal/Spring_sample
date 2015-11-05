package pl.demo.core.service.authentication;

import org.hibernate.validator.constraints.NotBlank;
import pl.demo.core.model.entity.Authentication;
import pl.demo.web.dto.TokenDTO;

import javax.validation.constraints.NotNull;

/**
 * Created by robertsikora on 05.11.2015.
 */
public interface AuthenticationService {

    @NotNull
    TokenDTO authenticate(@NotNull @NotBlank String username, @NotNull @NotBlank String password);

    @NotNull
    Authentication getAuthenticatedUser();

}
