package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pl.demo.core.model.entity.AuthenticationUserDetails;
import pl.demo.core.model.entity.User;
import pl.demo.core.model.repo.RoleRepository;
import pl.demo.core.model.repo.UserRepository;

@Service
@Transactional(readOnly=true)
public class UserServiceImpl extends CRUDServiceImpl<Long, User> implements UserService {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;

	@Autowired
	private UserServiceImpl(final RoleRepository roleRepository, final UserRepository userRepository, final PasswordEncoder passwordEncoder){
		super(userRepository);
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void edit(final User user) {
		Assert.notNull(user, "User is required");
		final User existing = userRepository.findByUsername(user.getUsername());
		Assert.state(null!=user, "User doesn't exist in db");
		existing.setName(user.getName());
		existing.setLocation(user.getLocation());
		existing.setPhone(user.getPhone());
		userRepository.save(existing);
	}

	@Override
	public User save (final User user){
		Assert.notNull(user, "User is required");
		user.setPassword( passwordEncoder.encode(user.getPassword()) );
		user.addRole(roleRepository.findByRoleName("user"));
		return super.save(user);
	}

	/*
		Releated to security
	 */
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		Assert.notNull(username, "Username is required");
		final User user = userRepository.findByUsername(username);
		if (null == user) {
			throw new UsernameNotFoundException("The user with name: " + username + " was not found");
		}
		return new AuthenticationUserDetails(user);
	}

	@Override
	public UserDetails authenticate(final String username, final String password){
		Assert.notNull(username, "Username is required");
		Assert.notNull(password, "Password is required");
		final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		final Authentication authentication = authManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return loadUserByUsername(username);
	}

	@Override
	public User getLoggedUser() {
		User loggedUser = null;
		final AuthenticationUserDetails userDetails = getLoggedUserDetails();
		if (userDetails != null) {
			loggedUser = userRepository.findOne(userDetails.getId());
			Assert.state(null!=loggedUser, "User doesn't exist in db");
		}
		return loggedUser;
	}

	@Override
	public AuthenticationUserDetails getLoggedUserDetails() {
		AuthenticationUserDetails loggedUserDetails = null;
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(authentication)) {
			final Object principal = authentication.getPrincipal();
			if (principal instanceof AuthenticationUserDetails) {
				loggedUserDetails = ((AuthenticationUserDetails) principal);
			}
		}
		return loggedUserDetails;
	}

	private boolean isAuthenticated(final Authentication authentication) {
		return authentication != null
				&& !(authentication instanceof AnonymousAuthenticationToken)
				&& authentication.isAuthenticated();
	}
}
