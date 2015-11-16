package pl.demo.core.service.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.demo.core.util.Assert;

/**
 * Created by robertsikora on 04.11.2015.
 */
public class AuthenticationContextProvider {

    private AuthenticationContextProvider(){}

    public static void setAuthentication(final Authentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void clearSecurityContext(){
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public static SecurityUser getAuthenticatedUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assert.notNull(authentication, "Lack of logged user in security context !");
        Assert.isTrue(!(authentication instanceof AnonymousAuthenticationToken));
        Assert.isTrue(authentication.isAuthenticated());

        return (SecurityUser) authentication.getPrincipal();
    }

    public static boolean isAuthenticatedUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return null != authentication
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }
}
