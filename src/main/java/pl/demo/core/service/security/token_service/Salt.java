package pl.demo.core.service.security.token_service;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Prototype
 * Created by robertsikora on 07.11.2015.
 */
public final class Salt {

    private final static String ALGORITHM = "SHA1PRNG";

    private static Random random;
    static {
        try {
            random = SecureRandom.getInstance(ALGORITHM);
        }catch (NoSuchAlgorithmException ex){
            throw new ExceptionInInitializerError(ex);
        }
    }
    private final byte[] salt = new byte[16];

    public Salt generate(){
        random.nextBytes(salt);
        return this;
    }

    public byte[] getValue(){
        return salt;
    }

    @Override
    public String toString(){
      return new String(getValue(), Charset.forName("ISO8859-1"));
    }
}
