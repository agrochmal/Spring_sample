package pl.demo.web.validator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import pl.demo.ApplicationContextFake;
import pl.demo.core.util.SpringBeanProvider;
import pl.demo.web.exception.ValidationRequestException;

/**
 * Created by robertsikora on 15.09.15.
 */

@RunWith(MockitoJUnitRunner.class)
public class ImageUploadValidatorTest {

    @InjectMocks
    private ImageUploadValidator testTarget;


    @Ignore
    @Test(expected = ValidationRequestException.class)
    public void testValidate() throws Exception {
        final MultipartFile multipartFile = createMultipartFile();
        testTarget.validate(multipartFile);
    }

    private MultipartFile createMultipartFile(){
        return new CommonsMultipartFile(new FileItemFake());
    }
}