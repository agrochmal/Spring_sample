package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.core.service.ResourceMediaService;
import pl.demo.core.util.Utils;
import pl.demo.web.validator.ImageUploadValidator;

import static pl.demo.web.EndpointConst.IMAGE.IMAGE_ENDPOINT;
import static pl.demo.web.EndpointConst.IMAGE.IMAGE_UPLOAD;

/**
 * Created by robertsikora on 26.08.15.
 */
@RestController
@RequestMapping(IMAGE_ENDPOINT)
public class ImageRestResource{

    private ResourceMediaService resourceMediaService;
    private ImageUploadValidator imageUploadValidator;

    @RequestMapping(value = IMAGE_UPLOAD,
            method = RequestMethod.POST)

    public ResponseEntity<?> uploadImage(@RequestParam("file") final MultipartFile file) {
        this.imageUploadValidator.validate(file);
        this.resourceMediaService.uploadImage(Utils.getBytes(file));
        return ResponseEntity.ok().build();
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
