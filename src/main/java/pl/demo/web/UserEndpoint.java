package pl.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.demo.core.model.entity.Advert;

import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.demo.web.USER.ENDPOINT;
import static pl.demo.web.USER.USER_FIND_ADVERTS;

/**
 * Created by robertsikora on 11.10.15.
 */

@RequestMapping(ENDPOINT)
public interface UserEndpoint {

    @RequestMapping(value = USER_FIND_ADVERTS, method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Collection<Advert>> findUserAdverts(@PathVariable Long userId);
}
