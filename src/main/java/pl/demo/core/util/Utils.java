package pl.demo.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.MsgConst;
import pl.demo.web.exception.ServerException;

import java.io.IOException;


public final class Utils {

	private final static Logger LOGGER = LoggerFactory.getLogger(Utils.class);

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

	public static byte[] getBytes(final MultipartFile file){
		Assert.notNull(file);
		byte[] bytes;
		try {
			bytes = file.getBytes();
		} catch (final IOException e) {
			LOGGER.error("Error during retrive bytes", e);
			throw new ServerException(MsgConst.FATAL_ERROR, e);
		}
		return bytes;
	}
}
