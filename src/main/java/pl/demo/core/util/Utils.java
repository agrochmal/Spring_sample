package pl.demo.core.util;

import org.springframework.validation.BindingResult;

public final class Utils {

	private Utils(){}
	
	public static String createErrorMessage(BindingResult bindingResult){
		StringBuilder str = new StringBuilder();
		bindingResult.getFieldErrors().forEach(
			t ->  str.append(t.getDefaultMessage()).append("\n")
		);
		return str.toString();
	}

	public static String concatStrings(String arg1, String arg2){

		return arg1.concat(arg2);
	}
}
