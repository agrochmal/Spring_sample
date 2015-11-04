package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.core.service.basic_service.CRUDService;
import pl.demo.core.service.resource.ResourceMediaService;
import pl.demo.web.validator.BusinessValidator;

/**
 * Created by robertsikora on 26.08.15.
 */
@RestController
public class ImageResourceImpl implements ImageResource {

    private CRUDService     domainService;
    private BusinessValidator validator;

    @Override
    public ResponseEntity<?> uploadImage(@RequestParam("file") final MultipartFile file) {
        this.validator.validate(file);
        final Long imageId = this.getResourceMediaService().upload(file);
        return ResponseEntity.ok(imageId);
    }

    @Override
    public ResponseEntity<?> deleteImage(@PathVariable final long id) {
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
    public void setValidator(final BusinessValidator validator) {
        this.validator = validator;
    }
}
