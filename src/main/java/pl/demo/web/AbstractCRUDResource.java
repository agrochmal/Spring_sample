package pl.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.demo.core.model.entity.BaseEntity;
import pl.demo.core.service.CRUDService;
import pl.demo.core.util.EntityUtils;
import pl.demo.core.util.Utils;
import pl.demo.web.exception.ResourceNotFoundException;

import javax.validation.Valid;
import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by Robert on 09.12.14.
 */
public abstract class AbstractCRUDResource<PK extends Serializable, E extends BaseEntity> {

    private static final String HEADER_ETAG = "ETag";

    private final CRUDService<PK, E> crudService;

    public AbstractCRUDResource(final CRUDService<PK, E> crudService){
        this.crudService = crudService;
    }

    /**
     * Get all resources
     * @return status 200 OK
     */

    @RequestMapping(method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)
    protected ResponseEntity<?> getResources(){
        final Collection<E> collection = this.crudService.findAll();
        if(null == collection) {
            throw new ResourceNotFoundException("Cannot find resources");
        }
        return ResponseEntity.ok().body(collection);
    }

    /**
     * Get resource by resource Id
     * @param id
     * @return status 200 OK
     */

    @RequestMapping(value="{id}",
            method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)
    protected ResponseEntity<?> getResourceById(@PathVariable final PK id){
        final E entity = this.crudService.findOne(id);
        if(null == entity) {
            throw new ResourceNotFoundException("Cannot find resource id:"+id);
        }
        return ResponseEntity.ok().header(HEADER_ETAG, String.valueOf(entity.hashCode())).body(entity);
    }

    /**
     * Delete the resource by Id
     * @param id
     * @return status 204 No Content
     */

    @RequestMapping(value="{id}",
            method = RequestMethod.DELETE)
    protected ResponseEntity<?> deleteResource(@PathVariable final PK id){
        crudService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Edit resource
     * @param entity
     * @param bindingResult
     * @return status 204 No Content
     */
    @RequestMapping(value="{id}",
            method = RequestMethod.PUT)
    protected ResponseEntity<?> editResource(@PathVariable final PK id, @Valid @RequestBody final E entity,
                                          final BindingResult bindingResult){
        EntityUtils.applyValidation(bindingResult);
        crudService.edit(id, entity);
        return ResponseEntity.noContent().build();
    }

    /** We put Location on Response Header.
     * Another approach is using a HATEOS and Atomic Links (links located in response body)
     * Method save given entity
     * @param entity
     * @param bindingResult
     * @return status 201 Created
     */

    @RequestMapping(method = RequestMethod.POST)
    protected ResponseEntity<?> save(@Valid @RequestBody final E entity, final BindingResult bindingResult) {
        EntityUtils.applyValidation(bindingResult);
        return Optional.ofNullable(crudService.save(entity))
                .map(t -> {
                    final URI uriLocation = Utils.createURI(t.getId().toString());
                    return ResponseEntity.created(uriLocation).build();}
                ).orElseGet(() -> ResponseEntity.noContent().build());
    }
}
