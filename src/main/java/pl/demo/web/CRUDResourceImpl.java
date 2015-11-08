package pl.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.demo.core.model.entity.BaseEntity;
import pl.demo.core.service.basic_service.CRUDService;
import pl.demo.core.util.Assert;
import pl.demo.core.util.WebUtils;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;


/**
 * Created by Robert on 09.12.14.
 */
public abstract class CRUDResourceImpl<PK extends Serializable, E extends BaseEntity>
    implements CRUDResource<PK, E>{

    private static final String HEADER_ETAG = "ETag";

    protected CRUDService<PK, E> crudService;

    @PostConstruct
    public void postConstruct(){
        Assert.notNull(crudService, "Initialization of crud service is mandatory!");
    }

    @Override
    public ResponseEntity<?> getResources(){
        final Collection<E> resources = this.crudService.findAll();
        Assert.notResourceFound(resources);

        return ResponseEntity.ok().body(resources);
    }

    @Override
    public ResponseEntity<?> getResourceById(@PathVariable final PK id){
        final E resource = this.crudService.findOne(id);
        Assert.notResourceFound(resource);

        return ResponseEntity.ok().header(HEADER_ETAG, String.valueOf(resource.hashCode())).body(resource);
    }

    @Override
    public ResponseEntity<?> deleteResource(@PathVariable final PK id){
        final E resource = this.crudService.findOne(id);
        Assert.notResourceFound(resource);
        this.crudService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> editResource(@PathVariable final PK id, @Valid @RequestBody final E entity,
                                          final BindingResult bindingResult){
        Assert.hasErrors(bindingResult);
        final E resource = this.crudService.findOne(id);
        Assert.notResourceFound(resource);
        this.crudService.edit(id, entity);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> save(@Valid @RequestBody final E entity, final BindingResult bindingResult) {
        Assert.hasErrors(bindingResult);

        return Optional.ofNullable(this.crudService.save(entity))
            .map(t -> ResponseEntity.created(WebUtils.createURI(t.getId())).build())
            .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
