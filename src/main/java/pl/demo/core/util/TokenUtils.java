package pl.demo.core.util;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class TokenUtils {

	private final static Logger logger = LoggerFactory.getLogger(TokenUtils.class);

	private static final String OBFUSCATE_KEY = "12dfR45At612GAn09";
	private static final long   TOKEN_TIME_VALIDITY_MS = 1000L * 60 * 5;	// 5 minutes

	private TokenUtils(){
		Assert.noObject();
	}

	public static String createToken(final UserDetails userDetails) {
		Assert.notNull(userDetails);
		long expires = System.currentTimeMillis() + TOKEN_TIME_VALIDITY_MS;
		final StringBuilder tokenBuilder = new StringBuilder();
		tokenBuilder.append(userDetails.getUsername());
		tokenBuilder.append(":");
		tokenBuilder.append(expires);
		tokenBuilder.append(":");
		tokenBuilder.append(TokenUtils.computeSignature(userDetails, expires));
		return tokenBuilder.toString();
	}

	private static String computeSignature(final UserDetails userDetails, final long expires) {
		Assert.notNull(userDetails);
		if(expires<=0){
			throw new IllegalArgumentException("Expires time should be positive");
		}
		final StringBuilder signatureBuilder = new StringBuilder();
		signatureBuilder.append(userDetails.getUsername());
		signatureBuilder.append(":");
		signatureBuilder.append(expires);
		signatureBuilder.append(":");
		signatureBuilder.append(userDetails.getPassword());
		signatureBuilder.append(":");
		signatureBuilder.append(OBFUSCATE_KEY);

		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (final NoSuchAlgorithmException e) {
			logger.error("Cannot find instance for MD5", e);
			Throwables.propagate(e);
		}
		return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes(Charset.forName("UTF-8")))));
	}

	public static String getUsernameFromToken(final String authToken) {
		if (null == authToken) {
			return null;
		}
		String[] parts = authToken.split(":");
		return parts[0];
	}

	public static boolean validateToken(final String authToken, final UserDetails userDetails) {
		Assert.hasText(authToken);
		Assert.notNull(userDetails);
		final String[] parts = authToken.split(":");
		long expires = Long.parseLong(parts[1]);
		final String signature = parts[2];
		if (expires < System.currentTimeMillis()) {
			return false;
		}
		return signature.equals(TokenUtils.computeSignature(userDetails, expires));
	}
}