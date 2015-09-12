package pl.demo.core.util;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import pl.demo.web.dto.TokenDTO;

@Ignore
public class UtilsTest extends TestCase {

    @Test
    public void testCreateErrorMessage() throws Exception {

        BindingResult br =  new BeanPropertyBindingResult(new TokenDTO(""), "");
        br.rejectValue("token", "", "Error occurs");

        assertEquals("Retrive a errors string from BindingResult object", "Error occurs\n", Utils.createErrorMessage(br));
    }
}