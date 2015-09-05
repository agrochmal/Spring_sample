package pl.demo.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.web.exception.ValidationRequestException;

/**
 * Created by robertsikora on 27.08.15.
 */

@Component
public class ImageUploadValidator implements Validator {

    private final static long MAX_FILE_SIZE = 10 * 1024;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
    }

    public void validate(Object target){
        final MultipartFile multipartFile = (MultipartFile)target;
        if(multipartFile.isEmpty()){
            throw new ValidationRequestException("File is empty");
        }
        if(multipartFile.getSize() > MAX_FILE_SIZE){
            throw new ValidationRequestException("File too big. Max size is 10MB");
        }
    }
}
