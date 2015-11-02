package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.core.service.CRUDService;
import pl.demo.core.service.ResourceMediaService;
import pl.demo.web.validator.CustomValidator;

/**
 * Created by robertsikora on 26.08.15.
 */
@RestController
public class ImageResourceImpl implements ImageResource {

    private CRUDService     domainService;
    private CustomValidator validator;

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
    public void setValidator(final CustomValidator validator) {
        this.validator = validator;
    }
}
