package pl.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.User;

import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.demo.web.EndpointConst.USER.*;

/**
 * Created by robertsikora on 11.10.15.
 */

@RequestMapping(USER_ENDPOINT)
public interface UserResource {

    @RequestMapping(value = USER_GET_LOGGED, method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<User> getLoggedUser();

    @RequestMapping(value = USER_IS_UNIQUE, method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Boolean> checkUnique(String username);

    @RequestMapping(value = USER_FIND_ADVERTS, method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Collection<Advert>> findUserAdverts(@PathVariable long userId);

    @RequestMapping(value = ACCOUNT, method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<User> findUserAccount(@PathVariable long userId);
}
