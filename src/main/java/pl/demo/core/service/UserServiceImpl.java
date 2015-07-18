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
import pl.demo.core.model.entity.AuthenticationUserDetails;
import pl.demo.core.model.entity.User;
import pl.demo.core.model.repo.RoleRepository;
import pl.demo.core.model.repo.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;

	@Autowired
	private UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User findOne(String username) {
		User user = userRepository.findByUsername(username);
		user.setAdverts(null);
		return user;
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(String username) {
		throw new RuntimeException("Not supported yet!");
	}

	@Override
	@Transactional(readOnly = false)
	public void edit(User user) {

		User existing = userRepository.findByUsername(user.getUsername());
		existing.setName(user.getName());
		existing.setLocation(user.getLocation());
		existing.setPhone(user.getPhone());
	}

	@Override
	@Transactional(readOnly = false)
	public User save (User user){
		user.setPassword( passwordEncoder.encode(user.getPassword()) );
		user.addRole(roleRepository.findByRoleName("user"));
		return userRepository.save(user);
	}

	/*
		Releated to security
	 */

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (null == user) {
			throw new UsernameNotFoundException("The user with name: " + username + " was not found");
		}
		return new AuthenticationUserDetails(user);
	}

	@Override
	public UserDetails authenticate(String username, String password){
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		Authentication authentication = authManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return loadUserByUsername(username);
	}

	@Override
	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	@Override
	public User getLoggedUser() {
		User loggedUser = null;
		AuthenticationUserDetails userDetails = getLoggedUserDetails();
		if (userDetails != null) {
			loggedUser = userRepository.findOne(userDetails.getId());
		}
		return loggedUser;
	}

	@Override
	public AuthenticationUserDetails getLoggedUserDetails() {
		AuthenticationUserDetails loggedUserDetails = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(authentication)) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof AuthenticationUserDetails) {
				loggedUserDetails = ((AuthenticationUserDetails) principal);
			}
		}
		return loggedUserDetails;
	}

	private boolean isAuthenticated(Authentication authentication) {
		return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
	}
}
