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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.demo.web.EndpointConst.USER.*;

@RestController
@RequestMapping(USER_ENDPOINT)
public class UserResourceImpl extends CRUDResourceImpl<Long, User> {

	private AdvertService   advertService;
	private CustomValidator validator;

	@RequestMapping(value = USER_GET_LOGGED,
			method = RequestMethod.GET,
			produces = APPLICATION_JSON_VALUE)

	public ResponseEntity<User> getLoggedUser() {
		final Optional<User> user = getUserService().getLoggedUser();
		return ResponseEntity.ok().body(user.isPresent() ? user.get() : null);
	}

	@RequestMapping(value = USER_IS_UNIQUE,
			method = RequestMethod.GET,
			produces = APPLICATION_JSON_VALUE)

	public ResponseEntity<Boolean> checkUnique(final String username) {
		return ResponseEntity.ok().body(validator.validate(username));
	}

	@RequestMapping(value = USER_AUTHENTICATE,
			method = RequestMethod.POST)

	public TokenDTO authenticate(@RequestParam("username") final String username, @RequestParam("password") final String password) {
		return new TokenDTO(TokenUtils.createToken(this.getUserService().authenticate(username, password)));
	}

	@RequestMapping(value = USER_FIND_ADVERTS,
            method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)

	public ResponseEntity<Collection<Advert>> findUserAdverts(@PathVariable final Long userId){
		return ResponseEntity.ok().body(this.advertService.findByUserId(userId));
	}

	@RequestMapping(value = ACCOUNT,
			method = RequestMethod.GET,
			produces = APPLICATION_JSON_VALUE)

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