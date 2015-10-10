package pl.demo;

import org.apache.commons.lang.StringUtils;
import pl.demo.core.util.MesssageResolver;

/**
 * Created by robertsikora on 10.10.15.
 */
public class MessageResolverFake implements MesssageResolver {

    @Override
    public String getMessage(String messageCode) {
        return StringUtils.EMPTY;
    }
}
