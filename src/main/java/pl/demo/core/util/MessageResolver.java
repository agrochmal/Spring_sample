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

    protected final static Locale pl_PL = new Locale("pl", "PL");
    private MessageSource messages;

    public String getMessage(final String messageCode){
        Assert.notNull(messageCode);
        return messages.getMessage(messageCode, null, pl_PL);
    }

    @Autowired
    public void setMessages(final MessageSource messages) {
        this.messages = messages;
    }
}
