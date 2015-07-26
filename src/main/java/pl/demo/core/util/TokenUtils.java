package pl.demo.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.util.StringUtils;

public final class TokenUtils {

	private static final String OBFUSCATE_KEY = "12dfR45At612GAn09";
	private static final long TOKEN_TIME_VALIDITY_MS = 1000L * 60 * 5;	// 5 minutes

	private TokenUtils(){
		throw new AssertionError("Cannot create object!");
	}

	public static String createToken(final UserDetails userDetails) {

		if(null == userDetails){
			throw new NullPointerException("Pass userDetails to function");
		}

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

		if(null == userDetails){
			throw new NullPointerException("Pass userDetails to function");
		}

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

		final MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("No MD5 algorithm available!");
		}

		return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
	}

	public static String getUsernameFromToken(final String authToken) {
		if (null == authToken) {
			return null;
		}

		String[] parts = authToken.split(":");
		return parts[0];
	}

	public static boolean validateToken(final String authToken, final UserDetails userDetails) {

		if(StringUtils.isEmpty(authToken)){
			throw new IllegalArgumentException("Pass authToken to function");
		}

		if(null == userDetails){
			throw new NullPointerException("Pass userDetails to function");
		}

		String[] parts = authToken.split(":");
		long expires = Long.parseLong(parts[1]);
		String signature = parts[2];

		if (expires < System.currentTimeMillis()) {
			return false;
		}

		return signature.equals(TokenUtils.computeSignature(userDetails, expires));
	}
}