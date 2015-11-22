package pl.demo.core.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import pl.demo.rules.FakeMessageResolverTestRule;
import pl.demo.web.exception.ResourceNotFoundException;
import pl.demo.web.exception.ValidationException;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * Created by robertsikora on 10.10.15.
 */

@RunWith(MockitoJUnitRunner.class)
public class AssertTest {

    private final static String SAMPLE_MESSAGE = "message";
    @Rule
    public FakeMessageResolverTestRule fakeMessageResolverTestRule = new FakeMessageResolverTestRule();
    @Mock(name="bindingResult")
    private BindingResult              bindingResult;

    @Test(expected = ResourceNotFoundException.class)
    public void testNotResourceFoundWithNullArg() {
        Assert.notResourceFound(null);
    }

    @Test
    public void testNotResourceFoundWithNotNullArg() {
        Assert.notResourceFound(new Object());
    }

    @Test
    public void testNotResourceFoundWithMessageWithNullArg() {
        try {
            Assert.notResourceFound(null, SAMPLE_MESSAGE);
            fail();
        }catch(final ResourceNotFoundException ex){
            assertEquals(SAMPLE_MESSAGE, ex.getMessageCode());
        }
    }

    @Test
    public void testNotResourceFoundWithMessageWithNotNullArg() {
        Assert.notResourceFound(new Object(), SAMPLE_MESSAGE);
    }

    @Test(expected = ValidationException.class)
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