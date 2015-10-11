package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.User;
import pl.demo.core.service.AdvertService;
import pl.demo.core.service.CRUDService;
import pl.demo.core.service.UserService;
import pl.demo.core.util.TokenUtils;
import pl.demo.web.dto.TokenDTO;
import pl.demo.web.validator.CustomValidator;
import java.util.Collection;
import java.util.Optional;
import static pl.demo.web.EndpointConst.USER.*;

@RestController
@RequestMapping(USER_ENDPOINT)
public class UserResourceImpl extends CRUDResourceImpl<Long, User> implements UserResource{

	private AdvertService   advertService;
	private CustomValidator validator;

	@Override
	public ResponseEntity<User> getLoggedUser() {
		final Optional<User> user = getUserService().getLoggedUser();
		return ResponseEntity.ok().body(user.isPresent() ? user.get() : null);
	}

	@Override
	public ResponseEntity<Boolean> checkUnique(final String username) {
		return ResponseEntity.ok().body(validator.validate(username));
	}

	@Override
	public TokenDTO authenticate(@RequestParam("username") final String username, @RequestParam("password") final String password) {
		return new TokenDTO(TokenUtils.createToken(this.getUserService().authenticate(username, password)));
	}

	@Override
	public ResponseEntity<Collection<Advert>> findUserAdverts(@PathVariable final Long userId){
		return ResponseEntity.ok().body(this.advertService.findByUserId(userId));
	}

	@Override
	public ResponseEntity<User> findUserAccount(@PathVariable final Long userId){
		return ResponseEntity.ok().body(this.getUserService().findOne(userId));
	}

	private UserService getUserService(){
		return (UserService)crudService;
	}

	@Autowired
	@Qualifier("userService")
	public void setDomainService(final CRUDService domainService) {
		this.crudService = domainService;
	}

	@Autowired
	@Qualifier("uniqueUserValidator")
	public void setValidator(final CustomValidator validator) {
		this.validator = validator;
	}

	@Autowired
	public void setAdvertService(final AdvertService advertService) {
		this.advertService = advertService;
	}
}