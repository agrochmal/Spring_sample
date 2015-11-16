package pl.demo.core.service.security.authentication;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.demo.core.service.security.SecurityUser;
import pl.demo.web.dto.Token;

import javax.validation.constraints.NotNull;

/**
 * Created by robertsikora on 05.11.2015.
 */

public interface AuthenticationService extends UserDetailsService {

    @NotNull
    Token authenticate(@NotBlank String username, @NotBlank String password);

    @PreAuthorize("isAuthenticated()")
    @NotNull
    SecurityUser getAuthenticatedUser();

    boolean isAuthenticatedUser();

    @PreAuthorize("isAuthenticated()")
    void logout();
}
