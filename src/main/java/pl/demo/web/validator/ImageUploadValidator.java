package pl.demo.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.MsgConst;
import pl.demo.web.exception.ValidationRequestException;

/**
 * Created by robertsikora on 27.08.15.
 */

@Component
public class ImageUploadValidator implements BusinessValidator {

    protected final static long MAX_FILE_SIZE = 10_000_000;

    @Override
    public boolean validate(final Object target){
        Assert.notNull(target);
        final MultipartFile multipartFile = (MultipartFile)target;
        if(multipartFile.isEmpty()){
            throw new ValidationRequestException(MsgConst.FILE_EMPTY);
        }
        if(multipartFile.getSize() > MAX_FILE_SIZE){
            throw new ValidationRequestException(MsgConst.FILE_TOO_BIG);
        }
        return true;
    }
}
