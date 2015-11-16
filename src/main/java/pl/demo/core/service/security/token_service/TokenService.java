package pl.demo.core.service.security.token_service;

import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by robertsikora on 07.11.2015.
 */

public interface TokenService {

    String generateToken(UserDetails userDetails);

    boolean authenticateByToken(String authToken, HttpServletRequest httpRequest);
}
