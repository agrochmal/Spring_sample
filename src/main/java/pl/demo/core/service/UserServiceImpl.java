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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pl.demo.MsgConst;
import pl.demo.core.model.entity.AuthenticationUserDetails;
import pl.demo.core.model.entity.User;
import pl.demo.core.model.repo.RoleRepository;
import pl.demo.core.model.repo.UserRepository;
import pl.demo.web.exception.ResourceNotFoundException;

public class UserServiceImpl extends CRUDServiceImpl<Long, User> implements UserService {

	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;

	@Override
	protected Class<User> supportedDomainClass() {
		return User.class;
	}

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;

	@Transactional
	@Override
	public void edit(final Long id, final User user) {
		Assert.notNull(user, "User is required");
		final User existing = getDomainRepository().findOne(id);
		Assert.state(null!=user, "User doesn't exist in db for id:"+id);
		existing.setName(user.getName());
		existing.setLocation(user.getLocation());
		existing.setPhone(user.getPhone());
		getDomainRepository().save(existing);
	}

	@Transactional
	@Override
	public User save (final User user){
		Assert.notNull(user, "User is required");
		user.setPassword( passwordEncoder.encode(user.getPassword()) );
		user.addRole(roleRepository.findByRoleName("user"));
		return super.save(user);
	}

	@Transactional
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		Assert.notNull(username, "Username is required");
		final User user = getUserRepository().findByUsername(username);
		if (null == user) {
			throw new ResourceNotFoundException(MsgConst.USER_NOT_FOUND);
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
			loggedUser = getDomainRepository().findOne(userDetails.getId());
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

	private UserRepository getUserRepository(){
		return (UserRepository)getDomainRepository();
	}

	@Autowired
	public void setRoleRepository(final RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Autowired
	public void setPasswordEncoder(final PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
}
