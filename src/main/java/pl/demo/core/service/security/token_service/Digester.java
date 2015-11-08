package pl.demo.core.service.security.token_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by robertsikora on 07.11.2015.
 */

@Component
public final class Digester {

    private final static Logger LOGGER = LoggerFactory.getLogger(Digester.class);

    private final static String ALGORITHM = "SHA-256";
    private final static int    ITERATIONS = 2048;

    final private MessageDigest messageDigest;{
        try {
            messageDigest = MessageDigest.getInstance(ALGORITHM);
        }catch (NoSuchAlgorithmException ex){
            LOGGER.error("Cannot find instance for " + ALGORITHM, ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public char[] hash(final String plainText) {
        byte [] value = Utf8.encode(plainText);
        synchronized (messageDigest) {
            for (int i = 0; i < ITERATIONS; i++) {
                value = messageDigest.digest(value);
            }
        }
        return Hex.encode(value);
    }
}
