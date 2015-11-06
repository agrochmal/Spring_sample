package pl.demo.core.service.validator;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.demo.core.service.user.UserService;
import pl.demo.web.exception.ResourceNotFoundException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by robertsikora on 11.10.15.
 */

@RunWith(MockitoJUnitRunner.class)
public class UniqueUserValidatorTest {

    private final static String USERNAME = "username";

    @InjectMocks
    private ExistUniqueUserValidator uniqueUserValidator;
    @Mock
    private UserService         userService;

    @Test(expected = IllegalArgumentException.class)
    public void testValidateWithNullArg() throws Exception {
        uniqueUserValidator.validate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateWithEmptyArg() throws Exception {
        uniqueUserValidator.validate(StringUtils.EMPTY);
    }

    @Test
    public void testValidateThrowingResourceNotFoundException() throws Exception {
        when(userService.loadUserByUsername(USERNAME)).thenThrow(ResourceNotFoundException.class);
        assertTrue(uniqueUserValidator.validate(USERNAME));
    }

    @Test
    public void testValidate() throws Exception {
        assertFalse(uniqueUserValidator.validate(USERNAME));
    }
}