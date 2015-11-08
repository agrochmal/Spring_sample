package pl.demo.core.service.security.authentication;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import pl.demo.core.model.entity.User;
import pl.demo.web.dto.TokenDTO;

import javax.validation.constraints.NotNull;

/**
 * Created by robertsikora on 05.11.2015.
 */
public interface AuthenticationService {

    @NotNull
    TokenDTO authenticate(@NotNull @NotBlank String username, @NotNull @NotBlank String password);

    @PreAuthorize("isAuthenticated()")
    @NotNull
    User getAuthenticatedUser();

    boolean isAuthenticatedUser();

}