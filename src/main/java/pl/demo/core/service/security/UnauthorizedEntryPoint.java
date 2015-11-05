package pl.demo.core.service.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * {@link AuthenticationEntryPoint} that rejects all requests with an
 * unauthorized error message.
 *
 */

public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

	private final static String UNAUTHORIZED = "Unauthorized: Authentication token was either missing or invalid.";

	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException)
			throws IOException, ServletException {

		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED);
	}

}