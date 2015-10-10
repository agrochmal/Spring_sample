package pl.demo.core.util;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import pl.demo.web.dto.TokenDTO;

import static org.junit.Assert.assertEquals;


public class UtilsTest {

    private final static String BEFORE_ESCAPING = "&<>" + Utils.getLineSeparator();
    private final static String AFTER_ESCAPING  = "&amp;&lt;&gt;<br/>";

    @Test
    public void testCreateErrorMessage() throws Exception {
        final BindingResult br =  new BeanPropertyBindingResult(new TokenDTO(StringUtils.EMPTY), StringUtils.EMPTY);
        br.rejectValue("token", "", "Error occurs");
        assertEquals("Retrive a errors string from BindingResult object", "Error occurs\n", Utils.createErrorMessage(br));
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

    @Test
    public void testCreateURI() throws Exception {

    }

    @Test
    public void testGetIpAdress() throws Exception {

    }

    @Test
    public void testGetBytes() throws Exception {

    }
}