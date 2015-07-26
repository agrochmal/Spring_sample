package pl.demo.core.util;

import org.springframework.validation.BindingResult;

import java.net.URI;
import java.net.URISyntaxException;

public final class Utils {

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
		final URI uri;
		try {
			uri = new URI(path);
		} catch (URISyntaxException ex) {
			throw new RuntimeException(ex);
		}
		return uri;
	}
}
