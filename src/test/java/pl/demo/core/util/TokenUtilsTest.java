package pl.demo.core.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;
import java.nio.charset.Charset;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenUtilsTest {

    private final static int    EXPIRES = 60;
    private final static String FAKE_TOKEN = "admin@admin.pl:1431784298750:4b4227fc85c4a08ef6f648b234a2c8f5";
    private final static String USER_NAME = "admin@admin.pl";
    private final static String PASSWORD = "76702638de8b35a1609f9cc19b744a3f726078a280b993fbf5fe4937985fcbbcbcc8b7099b448f91";
    private final static String DECODED_STRING = "admin@admin.pl:60:76702638de8b35a1609f9cc19b744a3f726078a280b9" +
                                                                "93fbf5fe4937985fcbbcbcc8b7099b448f91:12dfR45At612GAn09";
    private final static String DECODE_TOKEN;
    static {
        DECODE_TOKEN = new String(Hex.encode(Utils.getMessageDigest().digest(DECODED_STRING.getBytes(Charset.forName("UTF-8")))));
    }

    @Mock
    private UserDetails userDetails;

    @Test
    public void testGetUsernameFromToken() throws Exception {
        String result = TokenUtils.getUsernameFromToken(null);
        assertNull(result);
        result = TokenUtils.getUsernameFromToken(FAKE_TOKEN);
        assertEquals("admin@admin.pl", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeSignatureWithUserDetailsNull() throws Exception {
        TokenUtils.computeSignature(null, EXPIRES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeSignatureWith0Expires() throws Exception {
        TokenUtils.computeSignature(userDetails, 0);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testComputeSignatureWithMinusExpires() throws Exception {
        TokenUtils.computeSignature(userDetails, -1);
    }

    @Test
    public void testComputeSignature() throws Exception {
        when(userDetails.getUsername()).thenReturn(USER_NAME);
        when(userDetails.getPassword()).thenReturn(PASSWORD);
        assertEquals(DECODE_TOKEN, TokenUtils.computeSignature(userDetails, EXPIRES));
    }
}