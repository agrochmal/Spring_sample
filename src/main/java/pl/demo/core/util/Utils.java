package pl.demo.core.util;

import org.springframework.validation.BindingResult;

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
}
