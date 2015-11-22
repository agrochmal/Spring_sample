package pl.demo.web.exception;

import org.springframework.util.Assert;
import pl.demo.core.util.MessageResolver;
import pl.demo.core.util.SpringBeanProvider;

/**
 * Created by robertsikora on 08.09.15.
 */
public class AbstractException extends RuntimeException{

    private String messageCode;

    protected AbstractException(final String messageCode, final Throwable cause){
        super(getMessage(messageCode), cause);
        this.messageCode = messageCode;
    }

    private static String getMessage(final String messageCode){
        Assert.hasText(messageCode);
        final MessageResolver messageResolver = (MessageResolver) SpringBeanProvider.getBean("msgResolver");
        Assert.notNull(messageResolver, "Bean should exists in Spring context");
        return messageResolver.getMessage(messageCode);
    }

    public String getMessageCode() {
        return messageCode;
    }
}
