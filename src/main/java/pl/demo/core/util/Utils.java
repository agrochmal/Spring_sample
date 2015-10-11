package pl.demo.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.MsgConst;
import pl.demo.web.exception.GeneralException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Utils {

	private final static Logger LOGGER = LoggerFactory.getLogger(Utils.class);

	protected final static String X_FORWARDED_FOR_HEADER = "X-FORWARDED-FOR";

	private Utils(){
		Assert.noObject();
	}

	private final static String LINE_SEPARATOR;

	static{
		LINE_SEPARATOR = System.getProperty("line.separator");
		Assert.notNull(LINE_SEPARATOR);
	}

	public static String getLineSeparator(){
		return LINE_SEPARATOR;
	}

	public static String escapeHtml(final String text) {
		Assert.hasText(text, "Text cannot be empty!");
		return text.replace("&", "&amp;")
				   .replace("<", "&lt;")
				   .replace(">", "&gt;")
				   .replace(LINE_SEPARATOR, "<br/>");
	}
	
	public static String createErrorMessage(final BindingResult bindingResult){
		Assert.notNull(bindingResult);
		final StringBuilder str = new StringBuilder();
		bindingResult.getFieldErrors().forEach(
			t ->  str.append(t.getDefaultMessage()).append(LINE_SEPARATOR)
		);
		return str.toString();
	}

	public static URI createURI(final String path){
		Assert.hasText(path);
		URI uri;
		try {
			uri = new URI(path);
		} catch (final URISyntaxException e) {
			LOGGER.error("Cannot create URI for given path:"+path, e);
			throw new GeneralException(MsgConst.FATAL_ERROR, e);
		}
		return uri;
	}

	public static String getIpAdress(final HttpServletRequest httpServletRequest){
		Assert.notNull(httpServletRequest);
		String ipAddress = httpServletRequest.getHeader(X_FORWARDED_FOR_HEADER);
		if (ipAddress == null) {
			ipAddress = httpServletRequest.getRemoteAddr();
		}
		return ipAddress;
	}

	public static byte[] getBytes(final MultipartFile file){
		Assert.notNull(file);
		byte[] bytes;
		try {
			bytes = file.getBytes();
		} catch (final IOException e) {
			LOGGER.error("Error during retrive bytes", e);
			throw new GeneralException(MsgConst.FATAL_ERROR, e);
		}
		return bytes;
	}

	public static MessageDigest getMessageDigest() {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (final NoSuchAlgorithmException e) {
			LOGGER.error("Cannot find instance for MD5", e);
			throw new GeneralException(MsgConst.FATAL_ERROR, e);
		}
		return digest;
	}
}
