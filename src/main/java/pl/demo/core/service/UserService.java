package pl.demo.core.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.demo.core.model.entity.AuthenticationUserDetails;
import pl.demo.core.model.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert on 30.12.14.
 */

public interface UserService extends UserDetailsService, CRUDService<Long, User> {

    UserDetails authenticate(String username, String password);

    /**
     * Heavyweight method to get logged authentication.
     */
    User getLoggedUser();

    /**
     * Lightweight method to get currently logged authentication details
     */
    AuthenticationUserDetails getLoggedUserDetails();

    static Map<String, Boolean> createRoleMap(UserDetails userDetails) {
        final Map<String, Boolean> roles = new HashMap<>();
        for (final GrantedAuthority authority : userDetails.getAuthorities()) {
            roles.put(authority.getAuthority(), Boolean.TRUE);
        }
        return roles;
    }
}
