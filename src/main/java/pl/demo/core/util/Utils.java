package pl.demo.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.demo.MsgConst;
import pl.demo.web.exception.GeneralException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

public final class Utils {

	private final static Logger LOGGER = LoggerFactory.getLogger(Utils.class);

	protected final static String X_FORWARDED_FOR_HEADER = "X-FORWARDED-FOR";

	private Utils(){
		Assert.noObject();
	}

	public final static String LINE_SEPARATOR;

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

	public static URI createURI(final Serializable id){
		Assert.notNull(id);
		URI uri;
		try {
			uri = new URI(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/{id}").buildAndExpand(id).toString());
		} catch (final URISyntaxException e) {
			LOGGER.error("Cannot create URI for given id:" + id, e);
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
}
