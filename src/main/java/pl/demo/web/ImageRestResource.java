package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.core.service.CRUDService;
import pl.demo.core.service.ResourceMediaService;
import pl.demo.web.validator.CustomValidator;

import static pl.demo.web.EndpointConst.IMAGE.IMAGE_ENDPOINT;

/**
 * Created by robertsikora on 26.08.15.
 */
@RestController
@RequestMapping(IMAGE_ENDPOINT)
public class ImageRestResource{

    private CRUDService     domainService;
    private CustomValidator validator;

    @RequestMapping(method = RequestMethod.POST)

    public ResponseEntity<?> uploadImage(@RequestParam("file") final MultipartFile file) {
        this.validator.validate(file);
        final Long imageId = this.getResourceMediaService().upload(file);
        return ResponseEntity.ok(imageId);
    }

    @RequestMapping(value="{id}",
            method = RequestMethod.DELETE)

    public ResponseEntity<?> deleteImage(@PathVariable final Long id) {
        this.getResourceMediaService().deleteImage(id);
        return ResponseEntity.noContent().build();
    }

    private ResourceMediaService getResourceMediaService(){
        return (ResourceMediaService)domainService;
    }

    @Autowired
    @Qualifier("resourceMediaService")
    public void setDomainService(final CRUDService domainService) {
        this.domainService = domainService;
    }

    @Autowired
    @Qualifier("imageUploadValidator")
    public void setValidator(final CustomValidator validator) {
        this.validator = validator;
    }
}
