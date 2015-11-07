package pl.demo.core.service.security.token_service;

import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by robertsikora on 07.11.2015.
 */
public interface TokenGeneratorService {

    String generateToken(UserDetails userDetails);

    void authenticateByToken(String authToken, HttpServletRequest httpRequest);
}
