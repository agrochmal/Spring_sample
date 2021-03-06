package pl.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.demo.core.service.security.SecurityUser;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by robertsikora on 03.11.2015.
 */

@RequestMapping(AUTHENTICATE.ENDPOINT)
public interface AuthenticationEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity authenticate(@RequestParam("username") String username, @RequestParam("password") String password);

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<SecurityUser> getAuthenticatedUser();
}
