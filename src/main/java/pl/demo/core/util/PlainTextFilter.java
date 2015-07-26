package pl.demo.core.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by Robert on 22.02.15.
 */

@Component
public final class PlainTextFilter {

    public String escapeHtml(final String text) {
        if(StringUtils.isEmpty(text)){
            throw new IllegalArgumentException("Text cannot be empty!");
        }
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\n", "<br/>");
    }
}
