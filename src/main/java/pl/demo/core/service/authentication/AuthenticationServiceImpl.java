package pl.demo.core.service.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.demo.core.model.entity.Authentication;
import pl.demo.core.service.security.AuthenticationContextProvider;
import pl.demo.core.util.TokenUtils;
import pl.demo.web.dto.TokenDTO;


/**
 * Created by robertsikora on 05.11.2015.
 */

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authManager;

    @Override
    public TokenDTO authenticate(final String username, final String password) {
        final UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(username, password);
        final org.springframework.security.core.Authentication authentication
                = authManager.authenticate(authenticationToken);
        return new TokenDTO(TokenUtils.createToken((UserDetails)authentication.getPrincipal()));
    }

    @Override
    public Authentication getAuthenticatedUser() {
        return AuthenticationContextProvider.getAuthenticatedUser();
    }
}
