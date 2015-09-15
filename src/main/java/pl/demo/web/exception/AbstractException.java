package pl.demo.web.exception;

import org.springframework.util.Assert;
import pl.demo.core.util.MessageResolver;
import pl.demo.core.util.SpringBeanProvider;

/**
 * Created by robertsikora on 08.09.15.
 */
public class AbstractException extends RuntimeException{

    protected AbstractException(final String message, final Throwable cause){
        super(getMessage(message), cause);
    }

    protected static String getMessage(final String msgCode){
        Assert.hasText(msgCode);
        final MessageResolver messageResolver = (MessageResolver) SpringBeanProvider.getBean("msgResolver");
        Assert.notNull(messageResolver, "Bean should exists in Spring context");
        return messageResolver.getMessage(msgCode);
    }
}
