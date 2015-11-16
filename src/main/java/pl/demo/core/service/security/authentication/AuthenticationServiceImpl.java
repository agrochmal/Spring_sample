package pl.demo.core.service.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.MsgConst;
import pl.demo.core.model.entity.User;
import pl.demo.core.model.repo.UserRepository;
import pl.demo.core.service.registration.AccountStatus;
import pl.demo.core.service.security.AuthenticationContextProvider;
import pl.demo.core.service.security.SecurityUser;
import pl.demo.core.service.security.token_service.Salt;
import pl.demo.core.util.Assert;
import pl.demo.web.dto.Token;


/**
 * Created by robertsikora on 05.11.2015.
 */

public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager       authManager;
    @Autowired
    private TokenService                tokenGeneratorService; // TO-DO eliminate from here
    @Autowired
    private UserRepository              userRepository;

    @Transactional(readOnly = true)
    @Override
    public Token authenticate(final String username, final String password) {

        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authManager.authenticate(authenticationToken);

        AuthenticationContextProvider.setAuthentication(authentication);

        final SecurityUser securityUser = (SecurityUser)authentication.getPrincipal();
        final String salt = generateSalt().getValue().toString();
        updateUserSalt(salt, securityUser);

        return new Token(tokenGeneratorService.allocateToken(null).getKey());
    }

    @Override
    public SecurityUser getAuthenticatedUser() {
        return AuthenticationContextProvider.getAuthenticatedUser();
    }

    @Override
    public boolean isAuthenticatedUser() {
        return AuthenticationContextProvider.isAuthenticatedUser();
    }

    @Override
    public void logout() {
        final SecurityUser securityUser = getAuthenticatedUser();
        clearUserSalt(securityUser);
        AuthenticationContextProvider.clearSecurityContext();
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Assert.notNull(username, "Username is required");
        final User user = userRepository.findByUsername(username, AccountStatus.ACTIVE);
        Assert.notResourceFound(user, MsgConst.USER_NOT_FOUND);

        return new SecurityUser(user.getId(),
                                user.getContact().getEmail(),
                                user.getPassword(),
                                user.getAuthorities(),
                                user.getSalt());
    }

    private Salt generateSalt(){
        return new Salt().generate();
    }

    private void updateUserSalt(final String salt, final SecurityUser securityUser){
        Assert.isTrue(userRepository.updateUserSalt(salt, securityUser.getId()) > 0, "The salt wasn't be updated !");
        securityUser.setSalt(salt);
    }

    private void clearUserSalt(final SecurityUser securityUser){
        Assert.isTrue(userRepository.updateUserSalt(null, securityUser.getId()) > 0, "The salt wasn't be updated !");
        securityUser.setSalt(null);
    }
}
