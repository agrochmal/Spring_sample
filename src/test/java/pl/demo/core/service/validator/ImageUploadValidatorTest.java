package pl.demo.core.service.validator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.rules.FakeMessageResolverTestRule;
import pl.demo.web.exception.ValidationRequestException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by robertsikora on 15.09.15.
 */

@RunWith(MockitoJUnitRunner.class)
public class ImageUploadValidatorTest {

    @Rule
    public FakeMessageResolverTestRule fakeMessageResolverTestRule = new FakeMessageResolverTestRule();

    private BusinessValidator validator = new ImageUploadValidator();
    @Mock
    private MultipartFile              multipartFile;

    @Test(expected = IllegalArgumentException.class)
    public void testValidateWithNullArg() throws Exception {
        validator.validate(null);
    }

    @Test(expected = ValidationRequestException.class)
    public void testValidateWithEmptyFile() throws Exception {
        when(multipartFile.isEmpty()).thenReturn(true);
        validator.validate(multipartFile);
    }

    @Test(expected = ValidationRequestException.class)
    public void testValidateWithInvalidSize() throws Exception {
        when(multipartFile.getSize()).thenReturn(ImageUploadValidator.MAX_FILE_SIZE + 1);
        validator.validate(multipartFile);
    }

    @Test
    public void testValidate() throws Exception {
        when(multipartFile.getSize()).thenReturn(100L);
        assertTrue(validator.validate(multipartFile));
    }
}