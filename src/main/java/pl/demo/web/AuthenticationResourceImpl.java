package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.demo.core.service.user.UserService;
import pl.demo.core.util.TokenUtils;
import pl.demo.web.dto.TokenDTO;

/**
 * Created by robertsikora on 03.11.2015.
 */

@RestController
public class AuthenticationResourceImpl implements AuthenticationResource {

    private UserService userService;

    @Override
    public TokenDTO authenticate(@RequestParam("username") final String username, @RequestParam("password") final String password) {
        return new TokenDTO(TokenUtils.createToken(userService.authenticate(username, password)));
    }

    @Autowired
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }
}
