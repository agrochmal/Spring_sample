package pl.demo.core.service.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.MsgConst;
import pl.demo.core.model.entity.User;
import pl.demo.core.model.repo.UserRepository;
import pl.demo.core.service.registration.AccountStatus;
import pl.demo.core.service.security.AuthenticationContextProvider;
import pl.demo.core.service.security.SecurityUser;
import pl.demo.core.service.security.token_service.TokenGeneratorService;
import pl.demo.core.util.Assert;
import pl.demo.web.dto.TokenDTO;


/**
 * Created by robertsikora on 05.11.2015.
 */

public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager   authManager;
    @Autowired
    private TokenGeneratorService   tokenGeneratorService;
    @Autowired
    private UserRepository          userRepository;

    @Override
    public TokenDTO authenticate(final String username, final String password) {
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        final org.springframework.security.core.Authentication authentication = authManager.authenticate(authenticationToken);

        return new TokenDTO(tokenGeneratorService.generateToken((UserDetails)authentication.getPrincipal()));
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
        //TO-DO
        //clear security context
        //clear salt
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
}
