package pl.demo.core.util;

import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.validation.*;
import pl.demo.web.dto.TokenDTO;


public class UtilsTest extends TestCase {

    @Test
    public void testCreateErrorMessage() throws Exception {

        BindingResult br =  new BeanPropertyBindingResult(new TokenDTO(""), "");
        br.rejectValue("token", "", "Error occurs");

        assertEquals("Retrive a errors string from BindingResult object", "Error occurs\n", Utils.createErrorMessage(br));
    }

    @Test
    public void testConcatStrings() throws Exception {

        assertEquals("Method should return join strings", "aaabbb", Utils.concatStrings("aaa", "bbb"));

    }
}