package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.demo.core.model.entity.AuthenticationUserDetails;
import pl.demo.core.service.security.AuthenticationContextProvider;
import pl.demo.core.service.user.UserService;
import pl.demo.core.util.TokenUtils;
import pl.demo.web.dto.TokenDTO;

/**
 * Created by robertsikora on 03.11.2015.
 */

@RestController
public class AuthenticationEndpointImpl implements AuthenticationEndpoint {

    private UserService userService;

    @Override
    public TokenDTO authenticate(@RequestParam("username") final String username, @RequestParam("password") final String password) {
        return new TokenDTO(TokenUtils.createToken(userService.authenticate(username, password)));
    }

    @Override
    public ResponseEntity<AuthenticationUserDetails> getAuthenticatedUser() {
        return ResponseEntity.ok().body(AuthenticationContextProvider.getAuthenticatedUser());
    }

    @Autowired
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }
}
