package pl.demo.core.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.MsgConst;
import pl.demo.core.model.entity.Authentication;
import pl.demo.core.model.entity.RoleName;
import pl.demo.core.model.entity.User;
import pl.demo.core.model.repo.RoleRepository;
import pl.demo.core.model.repo.UserRepository;
import pl.demo.core.service.basic_service.CRUDServiceImpl;
import pl.demo.core.util.Assert;

public class UserServiceImpl extends CRUDServiceImpl<Long, User> implements UserService {

	private RoleRepository  roleRepository;
	private PasswordEncoder passwordEncoder;

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
		return new Authentication(user);
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
