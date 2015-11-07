package pl.demo.core.service.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import pl.demo.core.model.entity.Authentication;
import pl.demo.core.service.security.AuthenticationContextProvider;
import pl.demo.core.service.security.token_service.TokenGeneratorService;
import pl.demo.web.dto.TokenDTO;


/**
 * Created by robertsikora on 05.11.2015.
 */

public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authManager;
    @Autowired
    private TokenGeneratorService tokenGeneratorService;

    @Override
    public TokenDTO authenticate(final String username, final String password) {
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        final org.springframework.security.core.Authentication authentication = authManager.authenticate(authenticationToken);
        return new TokenDTO(tokenGeneratorService.generateToken((UserDetails)authentication.getPrincipal()));
    }

    @Override
    public Authentication getAuthenticatedUser() {
        return AuthenticationContextProvider.getAuthenticatedUser();
    }

    @Override
    public boolean isAuthenticatedUser() {
        return AuthenticationContextProvider.isAuthenticatedUser();
    }
}
