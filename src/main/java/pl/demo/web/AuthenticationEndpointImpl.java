package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.demo.core.model.entity.Authentication;
import pl.demo.core.service.security.authentication.AuthenticationService;
import pl.demo.web.dto.TokenDTO;

/**
 * Created by robertsikora on 03.11.2015.
 */

@RestController
public class AuthenticationEndpointImpl implements AuthenticationEndpoint {

    private AuthenticationService authenticationService;

    @Override
    public ResponseEntity<TokenDTO> authenticate(@RequestParam("username") final String username, @RequestParam("password") final String password) {
        return ResponseEntity.ok().body(authenticationService.authenticate(username, password));
    }

    @Override
    public ResponseEntity<Authentication> getAuthenticatedUser() {
        return ResponseEntity.ok().body(authenticationService.getAuthenticatedUser());
    }

    @Autowired
    public void setAuthenticationService(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
