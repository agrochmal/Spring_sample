package pl.demo.core.service.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

public class AuthenticationTokenProcessingFilter extends DelegatingFilterProxy {

	public static final String TOKEN_HEADER = "X-Auth-Token";
	public static final String TOKEN_QUERY_PARAM = "token";

	@Autowired
	private TokenService 	tokenGeneratorService;

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {

		final HttpServletRequest httpRequest = this.getAsHttpRequest(request);
		final String tokenKey = this.extractAuthTokenFromRequest(httpRequest);
		if(StringUtils.isNotBlank(tokenKey)) {
			tokenGeneratorService.verifyToken(tokenKey);
		}

		/**
		 *    Append a new authentication token to each http request
		 *    The alternative is to use HeaderModifierAdvice from here:
		 *    http://mtyurt.net/2015/07/20/spring-modify-response-headers-after-processing/
		 */
		final HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse)response) {
			@Override
			public void setStatus(int sc) {
				super.setStatus(sc);
				setXAuthToken(this);
			}
			@Override
			@SuppressWarnings("deprecation")
			public void setStatus(int sc, String sm) {
				super.setStatus(sc, sm);
				setXAuthToken(this);
			}
		};

		chain.doFilter(request, wrapper);
	}

	private HttpServletRequest getAsHttpRequest(final ServletRequest request) {
		if (!(request instanceof HttpServletRequest)) {
			throw new RuntimeException("Expecting an HTTP request");
		}
		return (HttpServletRequest) request;
	}

	private void setXAuthToken(final ServletResponse response) {
		if (!(response instanceof HttpServletResponse)) {
			throw new RuntimeException("Expecting an HTTP response");
		}
		if(AuthenticationContextProvider.isAuthenticatedUser()) {
			final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			httpServletResponse.addHeader(TOKEN_HEADER, tokenGeneratorService.allocateToken(null).getKey());
		}
	}

	private String extractAuthTokenFromRequest(final HttpServletRequest httpRequest) {
		String authToken = httpRequest.getHeader(TOKEN_HEADER);
		if (authToken == null) {
			authToken = httpRequest.getParameter(TOKEN_QUERY_PARAM);
		}
		return authToken;
	}
}