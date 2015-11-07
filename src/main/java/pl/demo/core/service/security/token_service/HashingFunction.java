package pl.demo.core.service.security.token_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;
import pl.demo.MsgConst;
import pl.demo.web.exception.GeneralException;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by robertsikora on 07.11.2015.
 */

@Component
public final class HashingFunction {

    private final static Logger LOGGER = LoggerFactory.getLogger(HashingFunction.class);

    private final static String ALGORITHM = "MD5";

    public char[] hash(final String plainText) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
            return Hex.encode(messageDigest.digest(plainText.getBytes(Charset.forName("UTF-8"))));
        } catch (final NoSuchAlgorithmException e) {
            LOGGER.error("Cannot find instance for " + ALGORITHM, e);
            throw new GeneralException(MsgConst.FATAL_ERROR, e);
        }
    }
}
