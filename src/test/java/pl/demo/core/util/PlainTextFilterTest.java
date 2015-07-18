package pl.demo.core.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlainTextFilterTest {

    private PlainTextFilter plainTextFilter = new PlainTextFilter();

    @Test
    public void testFilter() throws Exception {

        String input = "text &";
        String expected = "text &amp;";

        String result = plainTextFilter.escapeHtml(input);

        assertTrue( expected.equals(result));

    }
}