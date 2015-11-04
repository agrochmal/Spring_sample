package pl.demo.core.service.user;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.demo.core.model.entity.User;
import pl.demo.core.service.basic_service.CRUDService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by Robert on 30.12.14.
 */

public interface UserService extends UserDetailsService, CRUDService<Long, User> {

    @NotNull
    UserDetails authenticate(@NotNull @NotBlank @Email String email, @NotNull @NotBlank String password);

    @PreAuthorize("authentication.principal.username.equals(#user.entryUser)")
    void edit(Long id, @Valid @P("user") User user);
}
