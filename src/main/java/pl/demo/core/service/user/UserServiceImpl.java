package pl.demo.core.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.MsgConst;
import pl.demo.core.model.entity.User;
import pl.demo.core.model.repo.UserRepository;
import pl.demo.core.service.basic_service.CRUDServiceImpl;
import pl.demo.core.service.registration.AccountStatus;
import pl.demo.core.util.Assert;

public class UserServiceImpl extends CRUDServiceImpl<Long, User> implements UserService {

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

	/*
	 * For better performance use class SecurityUser
	 * 			extends org.springframework.security.core.userdetails.User
     *
	 */
	@Transactional
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		Assert.notNull(username, "Username is required");
		final User user = getUserRepository().findByUsername(username, AccountStatus.ACTIVE);
		Assert.notResourceFound(user, MsgConst.USER_NOT_FOUND);
		return user;
	}

	private UserRepository getUserRepository(){
		return (UserRepository) getDomainRepository();
	}
}
