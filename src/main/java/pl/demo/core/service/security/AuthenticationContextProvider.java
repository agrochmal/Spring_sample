package pl.demo.core.service.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.demo.core.model.entity.AuthenticationUserDetails;
import pl.demo.core.util.Assert;

/**
 * Created by robertsikora on 04.11.2015.
 */
public class AuthenticationContextProvider {

    private AuthenticationContextProvider(){}

    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static AuthenticationUserDetails getAuthenticatedUser() {
        final Authentication authentication = AuthenticationContextProvider.getAuthentication();
        Assert.notNull(authentication, "Lack of logged user in securoty context !");
        Assert.isTrue(!(authentication instanceof AnonymousAuthenticationToken));
        Assert.isTrue(authentication.isAuthenticated());
        return (AuthenticationUserDetails)authentication.getPrincipal();
    }
}
