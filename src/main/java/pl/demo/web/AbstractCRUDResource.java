package pl.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.demo.core.model.entity.BaseEntity;
import pl.demo.core.service.CRUDService;
import pl.demo.core.util.Utils;
import pl.demo.core.util.Assert;

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
        final Collection<E> resources = this.crudService.findAll();
        Assert.notResourceFound(resources);
        return ResponseEntity.ok().body(resources);
    }

    /**
     * Get resource by Id
     * @param id
     * @return status 200 OK
     */

    @RequestMapping(value="{id}",
            method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)

    protected ResponseEntity<?> getResourceById(@PathVariable final PK id){
        final E resource = this.crudService.findOne(id);
        Assert.notResourceFound(resource);
        return ResponseEntity.ok().header(HEADER_ETAG, String.valueOf(resource.hashCode())).body(resource);
    }

    /**
     * Delete the resource by Id
     * @param id
     * @return status 204 No Content
     */

    @RequestMapping(value="{id}",
            method = RequestMethod.DELETE)

    protected ResponseEntity<?> deleteResource(@PathVariable final PK id){
        final E resource = this.crudService.findOne(id);
        Assert.notResourceFound(resource);
        this.crudService.delete(id);
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
        Assert.hasErrors(bindingResult);
        final E resource = this.crudService.findOne(id);
        Assert.notResourceFound(resource);
        this.crudService.edit(id, entity);
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
        Assert.hasErrors(bindingResult);
        return Optional.ofNullable(this.crudService.save(entity))
            .map(t -> {
                final String path = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/{id}").buildAndExpand(t.getId()).toString();
                final URI uriLocation = Utils.createURI(path);
                return ResponseEntity.created(uriLocation).build();}
            ).orElseGet(() -> ResponseEntity.noContent().build());
    }
}
