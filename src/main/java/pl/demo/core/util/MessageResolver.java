package pl.demo.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Locale;

/**
 * Created by robertsikora on 06.09.15.
 */

@Component("msgResolver")
public class MessageResolver {

    private final static Locale PL = new Locale("pl", "PL");

    @Autowired
    private MessageSource messages;

    public String getMessage(final String messageCode){
        Assert.notNull(messageCode);
        return messages.getMessage(messageCode, null, PL);
    }

    public void setMessages(final MessageSource messages) {
        this.messages = messages;
    }
}
