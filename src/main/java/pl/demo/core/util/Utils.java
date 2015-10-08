package pl.demo.core.util;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.web.exception.ValidationRequestException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public final class Utils {

	private final static Logger LOGGER = LoggerFactory.getLogger(Utils.class);

	private Utils(){
		Assert.noObject();
	}

	private final static String LINE_SEPARATOR;

	static{
		LINE_SEPARATOR = System.getProperty("line.separator");
		Assert.notNull(LINE_SEPARATOR);
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
		URI uri=null;
		try {
			uri = new URI(path);
		} catch (URISyntaxException ex) {
			LOGGER.error("Cannot create URI for given path:"+path, ex);
			Throwables.propagate(ex);
		}
		return uri;
	}

	public static String getIpAdress(final HttpServletRequest httpServletRequest){
		Assert.notNull(httpServletRequest);
		String ipAddress = httpServletRequest.getHeader("X-FORWARDED-FOR");
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
		} catch (IOException e) {
			LOGGER.error("Error during retrive bytes", e);
			throw new ValidationRequestException("Cannot get bytes from uploaded image");
		}
		return bytes;
	}
}
