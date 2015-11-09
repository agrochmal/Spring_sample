package pl.demo.core.service.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.demo.core.util.Assert;

/**
 * Created by robertsikora on 04.11.2015.
 */
public class AuthenticationContextProvider {

    private AuthenticationContextProvider(){}

    public static org.springframework.security.core.Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static void setAuthentication(org.springframework.security.core.Authentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static SecurityUser getAuthenticatedUser() {
        final org.springframework.security.core.Authentication authentication = AuthenticationContextProvider.getAuthentication();
        Assert.notNull(authentication, "Lack of logged user in securoty context !");
        Assert.isTrue(!(authentication instanceof AnonymousAuthenticationToken));
        Assert.isTrue(authentication.isAuthenticated());

        return (SecurityUser) authentication.getPrincipal();
    }

    public static boolean isAuthenticatedUser() {
        final org.springframework.security.core.Authentication authentication = AuthenticationContextProvider.getAuthentication();
        return null != authentication
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }
}
