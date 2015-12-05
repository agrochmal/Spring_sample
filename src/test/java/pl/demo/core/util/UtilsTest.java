package pl.demo.core.util;

import org.apache.commons.lang.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.rules.FakeMessageResolverTestRule;
import pl.demo.web.dto.Token;
import pl.demo.web.exception.ServerException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {

    private final static String BEFORE_ESCAPING = "&<>" + Utils.getLineSeparator();
    private final static String AFTER_ESCAPING  = "&amp;&lt;&gt;<br/>";
    private final static String SAMPLE_IP  = "123.10.10.01";
    private final static String CORRECT_PATH = "\\sample";
    private final static String INCORRECT_PATH = "aaa-sample";

    @Rule
    public FakeMessageResolverTestRule fakeMessageResolverTestRule = new FakeMessageResolverTestRule();
    @Mock
    private MultipartFile              file;
    @Mock
    private HttpServletRequest         httpServletRequest;

    @Test
    public void testCreateErrorMessage() throws Exception {
        final BindingResult br =  new BeanPropertyBindingResult(new Token(StringUtils.EMPTY), StringUtils.EMPTY);
        br.rejectValue("token", "", "Error occurs");
        //assertEquals("Retrive a errors string from BindingResult object", "Error occurs\n", Utils.createErrorMessage(br));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeHtmlNullArg() throws Exception {
        Utils.escapeHtml(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeHtmlEmptyArg() throws Exception {
        Utils.escapeHtml(StringUtils.EMPTY);
    }

    @Test
    public void testEscapeHtml() throws Exception {
        assertEquals(AFTER_ESCAPING, Utils.escapeHtml(BEFORE_ESCAPING));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBytesWithNullArg() throws Exception {
        Utils.getBytes(null);
        Mockito.verifyZeroInteractions(file);
    }

    @Test(expected = ServerException.class)
    public void testGetBytesThrowIOException() throws Exception {
        when(file.getBytes()).thenThrow(IOException.class);
        Utils.getBytes(file);
    }

    @Test
    public void testGetBytes() throws Exception {
        final byte[] bytes = "abc".getBytes();
        when(file.getBytes()).thenReturn(bytes);
        assertEquals(bytes, Utils.getBytes(file));
    }
}