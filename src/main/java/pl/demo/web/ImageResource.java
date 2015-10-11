package pl.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by robertsikora on 11.10.15.
 */
public interface ImageResource {

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> uploadImage(@RequestParam("file") final MultipartFile file);

    @RequestMapping(value="{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteImage(@PathVariable Long id);
}
