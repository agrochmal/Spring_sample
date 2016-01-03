package pl.demo.core.service.user;

import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.model.entity.User;
import pl.demo.core.service.basicservice.CRUDServiceImpl;
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
}
