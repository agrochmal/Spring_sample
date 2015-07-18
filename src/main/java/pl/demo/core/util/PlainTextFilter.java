package pl.demo.core.util;

import org.springframework.stereotype.Component;

/**
 * Created by Robert on 22.02.15.
 */

@Component
public class PlainTextFilter {

    public String escapeHtml(String text) {

        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\n", "<br/>");
    }
}
