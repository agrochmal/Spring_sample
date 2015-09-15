package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.core.service.ResourceMediaService;
import pl.demo.web.validator.ImageUploadValidator;

import static pl.demo.web.EndpointConst.IMAGE.IMAGE_ENDPOINT;

/**
 * Created by robertsikora on 26.08.15.
 */
@RestController
@RequestMapping(IMAGE_ENDPOINT)
public class ImageRestResource {

    private ResourceMediaService resourceMediaService;
    private ImageUploadValidator imageUploadValidator;

    @RequestMapping(method = RequestMethod.POST)

    public ResponseEntity<?> uploadImage(@RequestParam("file") final MultipartFile file) {
        this.imageUploadValidator.validate(file);
        final Long imageId = this.resourceMediaService.upload(file);
        return ResponseEntity.ok(imageId);
    }

    @RequestMapping(value="{id}",
            method = RequestMethod.DELETE)

    public ResponseEntity<?> deleteImage(@PathVariable final Long id) {
        this.resourceMediaService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    public void setResourceMediaService(final ResourceMediaService resourceMediaService) {
        this.resourceMediaService = resourceMediaService;
    }

    @Autowired
    public void setImageUploadValidator(final ImageUploadValidator imageUploadValidator) {
        this.imageUploadValidator = imageUploadValidator;
    }
}
