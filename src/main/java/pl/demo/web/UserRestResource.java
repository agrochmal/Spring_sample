package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.User;
import pl.demo.core.service.AdvertService;
import pl.demo.core.service.UserService;
import pl.demo.core.util.TokenUtils;
import pl.demo.web.dto.TokenDTO;
import pl.demo.web.exception.ResourceNotFoundException;
import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.demo.web.EndpointConst.USER.*;

@RestController
@RequestMapping(USER_ENDPOINT)
public class UserRestResource extends AbstractCRUDResource<Long, User>{

	private final AdvertService advertService;
	private final UserService userService;

	@Autowired
	public UserRestResource(final UserService userService, final AdvertService advertService) {
		super(userService);
		this.userService = userService;
		this.advertService = advertService;
	}

	@RequestMapping(value = USER_GET_LOGGED,
			method = RequestMethod.GET,
			produces = APPLICATION_JSON_VALUE)

	public ResponseEntity<User> getLoggedUser() {
		final Optional<User> user = userService.getLoggedUser();
		return ResponseEntity.ok().body(user.isPresent() ? user.get() : null);
	}

	@RequestMapping(value = USER_IS_UNIQUE,
			method = RequestMethod.GET,
			produces = APPLICATION_JSON_VALUE)

	public ResponseEntity<Boolean> isUserExists(final String username) {
		try {
			this.userService.loadUserByUsername(username);
		}catch (final ResourceNotFoundException ex){
			return ResponseEntity.ok().body(Boolean.TRUE);
		}
		return ResponseEntity.ok().body(Boolean.FALSE);
	}

	@RequestMapping(value = USER_AUTHENTICATE,
			method = RequestMethod.POST)

	public TokenDTO authenticate(@RequestParam("username") final String username, @RequestParam("password") final String password) {
		return new TokenDTO(TokenUtils.createToken(this.userService.authenticate(username, password)));
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
		return ResponseEntity.ok().body(this.userService.findOne(userId));
	}
}