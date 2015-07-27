package pl.demo.core.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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


    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String && (principal).equals("anonymousUser"))
            return false;

        UserDetails userDetails = (UserDetails) principal;
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (authority.toString().equals("admin")) {
                return true;
            }
        }
        return false;
    }

    public static Map<String, Boolean> createRoleMap(UserDetails userDetails) {
        Map<String, Boolean> roles = new HashMap<>();
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            roles.put(authority.getAuthority(), Boolean.TRUE);
        }
        return roles;
    }
}
