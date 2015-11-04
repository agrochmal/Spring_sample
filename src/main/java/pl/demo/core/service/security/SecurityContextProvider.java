package pl.demo.core.service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by robertsikora on 04.11.2015.
 */
public class SecurityContextProvider {

    private SecurityContextProvider(){}

    public final static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
