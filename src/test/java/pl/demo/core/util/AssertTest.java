package pl.demo.core.util;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import pl.demo.ApplicationContextFake;
import pl.demo.MessageResolverFake;
import pl.demo.web.exception.ResourceNotFoundException;
import pl.demo.web.exception.ValidationRequestException;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by robertsikora on 10.10.15.
 */

@RunWith(MockitoJUnitRunner.class)
public class AssertTest {

    @Mock
    private BindingResult bindingResult;

    @BeforeClass
    public static void setupTest() throws Exception {
        SpringBeanProvider.setAppCtx(ApplicationContextFake.getApplicationContext(MessageResolverFake.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testNotResourceFoundNullArg() {
        Assert.notResourceFound(null);
    }

    @Test
    public void testNotResourceFound() {
        Assert.notResourceFound(new Object());
    }

    @Test
    public void testNotResourceFoundWithMessageNullArg() {
        try {
            Assert.notResourceFound(null, "message");
            fail();
        }catch(final ResourceNotFoundException ex){
            assertEquals("message", ex.getMessageCode());
        }
    }

    @Test
    public void testNotResourceFoundWithMessage() {
        Assert.notResourceFound(new Object(), "message");
    }

    @Test(expected = ValidationRequestException.class)
    public void testHasErrors() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(new FieldError("objectName", "field", "defaultMessage")));
        Assert.hasErrors(bindingResult);

    }

    @Test(expected = AssertionError.class)
    public void testNoObject() throws Exception {
        Assert.noObject();
    }
}