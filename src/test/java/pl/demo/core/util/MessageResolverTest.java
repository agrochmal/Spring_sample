package pl.demo.core.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by robertsikora on 15.09.15.
 */

@RunWith(MockitoJUnitRunner.class)
public class MessageResolverTest {

    @InjectMocks
    private MessageResolverImpl messageResolver;
    @Mock
    private MessageSource messageSource;

    @Test(expected = IllegalArgumentException.class)
    public void testGetMessageNullArg() {
        messageResolver.getMessage(null);
    }

    @Test(expected = NoSuchMessageException.class)
    public void testGetMessageNotFound() {
        when(messageSource.getMessage("any", null, MessageResolverImpl.pl_PL)).thenThrow(NoSuchMessageException.class);
        messageResolver.getMessage("any");
    }

    @Test
    public void testGetMessage() {
        final String message = "message1";
        when(messageSource.getMessage(message, null, MessageResolverImpl.pl_PL)).thenReturn(message);
        assertEquals(message, messageResolver.getMessage(message));
    }
}