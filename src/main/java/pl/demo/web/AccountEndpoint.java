package pl.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.demo.core.model.entity.User;

import javax.validation.Valid;

import static pl.demo.web.ACCOUNT.ENDPOINT;

/*
 * Created by robertsikora on 05.11.2015.
 */

@RequestMapping(ENDPOINT)
public interface AccountEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> registerAccount(@Valid @RequestBody final User user, final BindingResult bindingResult);

    @RequestMapping(method = RequestMethod.PUT)
    ResponseEntity<?> activateAccount(String activationCode);

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<Boolean> checkUniqueAccount(String username);

}
