package pl.demo.core.service.user;

import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import pl.demo.core.model.entity.User;
import pl.demo.core.service.basicservice.CRUDService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by Robert on 30.12.14.
 */

@Validated
public interface UserService extends CRUDService<Long, User> {

    @PreAuthorize("isAuthenticated()")
    //@PreAuthorize("authentication.principal.username.equals(#user.entryUser)")
    void edit(@NotNull Long id, @NotNull @Valid @P("user") User user);
}
