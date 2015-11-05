package pl.demo.core.service.user;

import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.demo.core.model.entity.User;
import pl.demo.core.service.basic_service.CRUDService;

import javax.validation.Valid;

/**
 * Created by Robert on 30.12.14.
 */

public interface UserService extends UserDetailsService, CRUDService<Long, User> {

    @PreAuthorize("authentication.principal.username.equals(#user.entryUser)")
    void edit(Long id, @Valid @P("user") User user);
}
