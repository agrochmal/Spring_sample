package pl.demo.web;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.demo.core.model.entity.BaseEntity;
import pl.demo.core.service.CRUDService;
import pl.demo.core.util.Utils;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by Robert on 09.12.14.
 */
public abstract class AbstractCRUDResource<PK, Entity extends BaseEntity> {

    private static final String HEADER_ETAG = "ETag";

    private CRUDService<PK, Entity> crudService;

    public AbstractCRUDResource(CRUDService<PK, Entity> crudService){
        this.crudService = crudService;
    }

    /**
     * Get all resources
     * @return status 200 OK
     */

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)

    protected ResponseEntity<?> getResources(){
        final Collection<Entity> collection = this.crudService.findAll();
        if(null==collection)
            throw new ResourceNotFoundException("Resources not found");

        return ResponseEntity.ok().body(collection);
    }

    /**
     * Get resource by resource Id
     * @param id
     * @return status 200 OK
     */

    @RequestMapping(value="{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)

    protected ResponseEntity<?> getResourceById(@PathVariable PK id){
        final Entity entity = this.crudService.findOne(id);
        if(null==entity)
            throw new ResourceNotFoundException("Resource not found");

        return ResponseEntity.ok().header(HEADER_ETAG, String.valueOf(entity.hashCode())).body(entity);
    }

    /**
     * Delete the resource by Id
     * @param id
     * @return status 204 No Content
     */

    @RequestMapping(value="{id}",
            method = RequestMethod.DELETE)

    protected ResponseEntity<?> deleteResource(@PathVariable PK id){

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

    public ResponseEntity<?> editResource(@PathVariable PK id,
                                          @Valid @RequestBody Entity entity,
                                          @RequestHeader HttpHeaders headers,
                                          BindingResult bindingResult){

        if(bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(Utils.createErrorMessage(bindingResult));

        crudService.edit(entity);

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

    public ResponseEntity<?> save(@Valid @RequestBody Entity entity, BindingResult bindingResult) {

        if(bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(Utils.createErrorMessage(bindingResult));

        return Optional.ofNullable(crudService.save(entity))
                .map(t -> {
                            URI uri = null;
                            try {
                                uri = new URI("/resources/" + t.getId());
                            } catch (URISyntaxException ex) {
                                throw new RuntimeException(ex);
                            }
                            return ResponseEntity.created(uri).build();
                        }
                ).orElseGet(() -> ResponseEntity.noContent().build());
    }

    /*  Common exception handling   */

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    String handleException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        List<String> errors = new ArrayList<>(fieldErrors.size() + globalErrors.size());
        String error;
        for (FieldError fieldError : fieldErrors) {
            error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
            errors.add(error);
        }
        for (ObjectError objectError : globalErrors) {
            error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
            errors.add(error);
        }
        return errors.toString();
    }
}

    class ResourceNotFoundException extends RuntimeException {

        public ResourceNotFoundException() {
            this("Resource not found!");
        }

        public ResourceNotFoundException(String message) {
            this(message, null);
        }

        public ResourceNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }
