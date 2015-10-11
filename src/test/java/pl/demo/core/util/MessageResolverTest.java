package pl.demo.core.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    private final static String SAMPLE_MESSAGE = "message";

    @InjectMocks
    private MessageResolverImpl messageResolver;
    @Mock
    private MessageSource       messageSource;

    @Test(expected = IllegalArgumentException.class)
    public void testGetMessageNullArg() {
        messageResolver.getMessage(null);
        Mockito.verifyZeroInteractions(messageSource);
    }

    @Test(expected = NoSuchMessageException.class)
    public void testGetMessageNotFound() {
        when(messageSource.getMessage(SAMPLE_MESSAGE, null, MessageResolverImpl.pl_PL)).thenThrow(NoSuchMessageException.class);
        messageResolver.getMessage(SAMPLE_MESSAGE);
    }

    @Test
    public void testGetMessage() {
        when(messageSource.getMessage(SAMPLE_MESSAGE, null, MessageResolverImpl.pl_PL)).thenReturn(SAMPLE_MESSAGE);
        assertEquals(SAMPLE_MESSAGE, messageResolver.getMessage(SAMPLE_MESSAGE));
    }
}