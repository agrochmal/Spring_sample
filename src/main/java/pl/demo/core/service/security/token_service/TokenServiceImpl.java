package pl.demo.core.service.security.token_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.service.security.AuthenticationContextProvider;
import pl.demo.core.service.security.SecurityUser;
import pl.demo.core.service.security.authentication.AuthenticationService;
import pl.demo.core.util.Assert;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;


/**
 * Created by robertsikora on 07.11.2015.
 */

@Component
public class TokenServiceImpl implements TokenService {

    @Value("${TOKEN.expiration-time?: 60}")
    private int             MINUTES;
    private long            TOKEN_TIME_VALIDITY_MS ;

    @Autowired
    private AuthenticationService   authenticationService;

    @Autowired
    private Digester                digester;

    @PostConstruct
    private void calculateTokenExpirationTime(){
        this.TOKEN_TIME_VALIDITY_MS = 1000L * 60 * MINUTES;
    }

    @Override
    public String generateToken(final UserDetails userDetails) {
        Assert.notNull(userDetails);

        final long expires = countExpirationTokenTime();
        final StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append(userDetails.getUsername());
        tokenBuilder.append(":");
        tokenBuilder.append(expires);
        tokenBuilder.append(":");
        tokenBuilder.append(computeSignature(userDetails, expires, retrieveUserSalt(userDetails)));

        return tokenBuilder.toString();
    }

    private long countExpirationTokenTime(){
        return System.currentTimeMillis() + TOKEN_TIME_VALIDITY_MS;
    }

    /*
    *
    * token [username:expires:ip_address:salt] -> save to db ?
     */
    private String computeSignature(final UserDetails userDetails, final long expires, final String salt) {
        if(expires<=0){
            throw new IllegalArgumentException("Expires time should be positive");
        }

        final String[] attributes = {
                userDetails.getUsername(),
                String.valueOf(expires),
                //webAuthenticationDetails.getRemoteAddress(),
                salt
        };

        final StringBuilder signatureBuilder = new StringBuilder();
        for(final String attribute : attributes){
            signatureBuilder.append(attribute);
        }
        return new String(Hex.encode(digester.hash(signatureBuilder.toString())));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean authenticateByToken(final String authToken, final HttpServletRequest httpRequest) {
        Assert.hasText(authToken);

        final String username = resolveToken(authToken);
        if (username != null) {
            final UserDetails userDetails = authenticationService.loadUserByUsername(username);
            if (validateToken(authToken, userDetails)) {
                final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                AuthenticationContextProvider.setAuthentication(authentication);
                return true;
            }
        }
        return false;
    }

    private String resolveToken(final String authToken) {
        return authToken.split(":")[0];
    }

    private boolean validateToken(final String authToken, final UserDetails userDetails){
        final String[] parts = authToken.split(":");
        final long expires = Long.parseLong(parts[1]);
        final String signature = parts[2];
        if (expires < System.currentTimeMillis()) {
            return false;
        }
        return signature.equals(computeSignature(userDetails, expires, retrieveUserSalt(userDetails)));
    }

    private String retrieveUserSalt(final UserDetails userDetails){
        final SecurityUser securityUser = (SecurityUser) userDetails;
        return securityUser.getSalt();
    }
}
