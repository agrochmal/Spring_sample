package pl.demo.core.service.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.DelegatingFilterProxy;
import pl.demo.core.service.security.token_service.TokenGeneratorService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthenticationTokenProcessingFilter extends DelegatingFilterProxy {

	@Autowired
	private TokenGeneratorService tokenGeneratorService;

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {

		final HttpServletRequest httpRequest = this.getAsHttpRequest(request);
		final String authToken = this.extractAuthTokenFromRequest(httpRequest);
		if(StringUtils.isNotBlank(authToken)) {
			tokenGeneratorService.authenticateByToken(authToken, httpRequest);
		}
		chain.doFilter(request, response);
	}

	private HttpServletRequest getAsHttpRequest(final ServletRequest request) {
		if (!(request instanceof HttpServletRequest)) {
			throw new RuntimeException("Expecting an HTTP request");
		}
		return (HttpServletRequest) request;
	}

	private String extractAuthTokenFromRequest(final HttpServletRequest httpRequest) {
		String authToken = httpRequest.getHeader("X-Auth-Token");
		if (authToken == null) {
			authToken = httpRequest.getParameter("token");
		}
		return authToken;
	}
}