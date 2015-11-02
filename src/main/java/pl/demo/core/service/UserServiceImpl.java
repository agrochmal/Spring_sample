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
import pl.demo.MsgConst;
import pl.demo.core.model.entity.AuthenticationUserDetails;
import pl.demo.core.model.entity.RoleName;
import pl.demo.core.model.entity.User;
import pl.demo.core.model.repo.RoleRepository;
import pl.demo.core.model.repo.UserRepository;
import pl.demo.core.util.Assert;

import java.util.Optional;

public class UserServiceImpl extends CRUDServiceImpl<Long, User> implements UserService {

	private RoleRepository  roleRepository;
	private PasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;

	@Transactional
	@Override
	public void edit(final Long id, final User user) {
		Assert.notNull(user, "User is required");
		final User existing = getDomainRepository().findOne(id);
		Assert.notResourceFound(existing);
		existing.setName(user.getName());
		existing.getContact().setLocation(user.getContact().getLocation());
		existing.getContact().setPhone(user.getContact().getPhone());
		getDomainRepository().save(existing);
	}

	@Transactional
	@Override
	public User save(final User user){
		Assert.notNull(user, "User is required");
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		user.addRole(this.roleRepository.findByRoleName(RoleName.USER_ROLE));
		user.setEntryUser(user.getContact().getEmail());
		return super.save(user);
	}

	@Transactional
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		Assert.notNull(username, "Username is required");
		final User user = getUserRepository().findByUsername(username);
		Assert.notResourceFound(user, MsgConst.USER_NOT_FOUND);
		return new AuthenticationUserDetails(user);
	}

	@Transactional(readOnly = true)
	@Override
	public UserDetails authenticate(final String username, final String password){
		Assert.notNull(username, "Username is required");
		Assert.notNull(password, "Password is required");
		final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		final Authentication authentication = authManager.authenticate(authenticationToken);
		return (UserDetails)authentication.getPrincipal();
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<User> getLoggedUser() {
		final Optional<AuthenticationUserDetails> userDetails = getLoggedUserDetails();
		if (userDetails.isPresent()) {
            final User result = new User();
			final User dbUser = getDomainRepository().findOne(userDetails.get().getId());
			Assert.notResourceFound(dbUser, MsgConst.USER_NOT_FOUND);
			result.setName(dbUser.getName());
			result.setId(dbUser.getId());
			result.setContact(dbUser.getContact());
			result.setRoles(dbUser.getRoles());
			return Optional.of(result);
		}
		return Optional.empty();
	}

	private Optional<AuthenticationUserDetails> getLoggedUserDetails() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(authentication)) {
			final Object principal = authentication.getPrincipal();
			if (principal instanceof AuthenticationUserDetails) {
				return Optional.of((AuthenticationUserDetails) principal);
			}
		}
		return Optional.empty();
	}

	private boolean isAuthenticated(final Authentication authentication) {
		return authentication != null
				&& !(authentication instanceof AnonymousAuthenticationToken)
				&& authentication.isAuthenticated();
	}

	private UserRepository getUserRepository(){
		return (UserRepository) getDomainRepository();
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
