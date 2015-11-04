package pl.demo.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.demo.web.dto.TokenDTO;

/**
 * Created by robertsikora on 03.11.2015.
 */

@RequestMapping(EndpointConst.AUTHENTICATE.ENDPOINT)
public interface AuthenticationResource {

    @RequestMapping(method = RequestMethod.POST)
    TokenDTO authenticate(@RequestParam("username") String username, @RequestParam("password") String password);
}
