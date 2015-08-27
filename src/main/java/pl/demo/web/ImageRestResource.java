package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.core.service.ResourceMediaService;
import pl.demo.core.util.EntityUtils;
import pl.demo.core.util.Utils;
import pl.demo.web.validator.ImageUploadValidator;

/**
 * Created by robertsikora on 26.08.15.
 */
@RestController
@RequestMapping("/resources")
public class ImageRestResource{

    private ResourceMediaService resourceMediaService;
    private ImageUploadValidator imageUploadValidator;

    @RequestMapping(value="/upload",
            method = RequestMethod.POST)

    public ResponseEntity<?> uploadImage(final MultipartFile file, final BindingResult bindingResult){
        this.imageUploadValidator.validate(file, bindingResult);
        EntityUtils.applyValidation(bindingResult);
        this.resourceMediaService.uploadImage(Utils.getbytes(file));
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
