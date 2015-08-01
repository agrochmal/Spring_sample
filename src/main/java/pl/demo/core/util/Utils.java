package pl.demo.core.util;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

public final class Utils {

	private final static Logger logger = LoggerFactory.getLogger(Utils.class);

	private Utils(){
		throw new AssertionError("Cannot create object!");
	}
	
	public static String createErrorMessage(final BindingResult bindingResult){
		if(null == bindingResult){
			throw new NullPointerException("Pass bindingResult to function");
		}
		final StringBuilder str = new StringBuilder();
		bindingResult.getFieldErrors().forEach(
			t ->  str.append(t.getDefaultMessage()).append("\n")
		);
		return str.toString();
	}

	public static URI createURI(final String path){
		URI uri=null;
		try {
			uri = new URI(path);
		} catch (URISyntaxException ex) {
			logger.error("Cannot create URI for given path:"+path, ex);
			Throwables.propagate(ex);
		}
		return uri;
	}

	public static String getIpAdress(final HttpServletRequest httpServletRequest){
		String ipAddress = httpServletRequest.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = httpServletRequest.getRemoteAddr();
		}
		return ipAddress;
	}
}
