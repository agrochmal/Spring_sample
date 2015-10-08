package pl.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.demo.core.model.entity.BaseEntity;
import javax.validation.Valid;
import java.io.Serializable;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by robertsikora on 08.10.15.
 */
public interface CRUDResource <PK extends Serializable, E extends BaseEntity>{

    /**
     * Get all resources
     * @return status 200 OK
     */

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)

    ResponseEntity<?> getResources();

    /**
     * Get resource by Id
     * @param id
     * @return status 200 OK
     */

    @RequestMapping(value="{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)

    ResponseEntity<?> getResourceById(@PathVariable PK id);

    /**
     * Delete the resource by Id
     * @param id
     * @return status 204 No Content
     */

    @RequestMapping(value="{id}", method = RequestMethod.DELETE)

    ResponseEntity<?> deleteResource(@PathVariable PK id);

    /**
     * Edit resource
     * @param entity
     * @param bindingResult
     * @return status 204 No Content
     */
    @RequestMapping(value="{id}", method = RequestMethod.PUT)

     ResponseEntity<?> editResource(@PathVariable PK id, @Valid @RequestBody E entity, BindingResult bindingResult);

    /** We put Location on Response Header.
     * Another approach is using a HATEOS and Atomic Links (links located in response body)
     * Method save given entity
     * @param entity
     * @param bindingResult
     * @return status 201 Created
     */

    @RequestMapping(method = RequestMethod.POST)

    ResponseEntity<?> save(@Valid @RequestBody E entity, BindingResult bindingResult);
}
