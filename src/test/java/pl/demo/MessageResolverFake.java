package pl.demo;

import org.apache.commons.lang.StringUtils;
import pl.demo.core.util.MessageResolver;

/**
 * Created by robertsikora on 10.10.15.
 */
public class MessageResolverFake implements MessageResolver {

    @Override
    public String getMessage(final String messageCode) {
        return StringUtils.EMPTY;
    }
}
