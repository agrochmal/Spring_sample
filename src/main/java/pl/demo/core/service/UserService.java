package pl.demo.core.service;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.demo.core.model.entity.User;

import javax.validation.constraints.NotNull;

/**
 * Created by Robert on 30.12.14.
 */

public interface UserService extends UserDetailsService, CRUDService<Long, User> {

    @NotNull
    UserDetails authenticate(@NotNull @NotBlank @Email String email, @NotNull @NotBlank String password);

    @NotNull
    User getLoggedUser();
}
