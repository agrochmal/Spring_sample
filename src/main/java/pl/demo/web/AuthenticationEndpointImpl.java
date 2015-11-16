package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.demo.core.service.security.SecurityUser;
import pl.demo.core.service.security.authentication.AuthenticationService;
import pl.demo.web.dto.Token;

/**
 * Created by robertsikora on 03.11.2015.
 */

@RestController
public class AuthenticationEndpointImpl implements AuthenticationEndpoint {

    private AuthenticationService authenticationService;

    @Override
    public ResponseEntity<Token> authenticate(@RequestParam("username") final String username, @RequestParam("password") final String password) {

        final Token token = authenticationService.authenticate(username, password);
        return new ResponseEntity(token, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SecurityUser> getAuthenticatedUser() {
        return ResponseEntity.ok().body(authenticationService.getAuthenticatedUser());
    }

    @Autowired
    public void setAuthenticationService(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
