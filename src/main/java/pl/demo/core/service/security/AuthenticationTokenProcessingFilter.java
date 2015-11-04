package pl.demo.core.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.DelegatingFilterProxy;
import pl.demo.core.util.TokenUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthenticationTokenProcessingFilter extends DelegatingFilterProxy {

	@Autowired
	private UserDetailsService userService;

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {

		final HttpServletRequest httpRequest = this.getAsHttpRequest(request);
		final String authToken = this.extractAuthTokenFromRequest(httpRequest);
		final String username = TokenUtils.getUsernameFromToken(authToken);
		if (username != null) {
			final UserDetails userDetails = userService.loadUserByUsername(username);
			if (TokenUtils.validateToken(authToken, userDetails)) {
				final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
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