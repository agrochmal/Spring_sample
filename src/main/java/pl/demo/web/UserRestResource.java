package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import pl.demo.MsgConst;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.AuthenticationUserDetails;
import pl.demo.core.model.entity.User;
import pl.demo.core.service.AdvertService;
import pl.demo.core.service.UserService;
import pl.demo.core.util.TokenUtils;
import pl.demo.web.dto.TokenDTO;
import pl.demo.web.dto.UserDTO;
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

	public ResponseEntity<UserDTO> getLoggedUser() {
		final AuthenticationUserDetails user = userService.getLoggedUserDetails();
		Assert.state(null!=user);
		return ResponseEntity.ok().body(UserDTO.UserDTOBuilder.anUserDTO()
				.withId(user.getId())
				.withUsername(user.getUsername())
				.withRoles(UserService.createRoleMap(user)).build());
	}

	@Override
	public ResponseEntity<UserDTO> getResourceById(@PathVariable Long id){
		return Optional.ofNullable(this.userService.getLoggedUser())
			.map(t -> {
						final UserDTO dto = UserDTO.UserDTOBuilder.anUserDTO()
								.withId(t.getId())
								.withUsername(t.getUsername())
								.withName(t.getName())
								.withLocation(t.getLocation())
								.withPhone(t.getPhone())
								.withRoles(UserService.createRoleMap(new AuthenticationUserDetails(t))).build();
						return ResponseEntity.ok().body(dto);
					}
			).orElseGet(() -> {
				throw new ResourceNotFoundException(MsgConst.USER_NOT_FOUND);
			});
	}

	@RequestMapping(value = USER_IS_UNIQUE,
			method = RequestMethod.GET,
			produces = APPLICATION_JSON_VALUE)

	public ResponseEntity<Boolean> isUserExists(final String username) {
		try {
			userService.loadUserByUsername(username);
		}catch (UsernameNotFoundException ex){
			return ResponseEntity.ok().body(Boolean.TRUE);
		}
		return ResponseEntity.ok().body(Boolean.FALSE);
	}

	@RequestMapping(value = USER_AUTHENTICATE,
			method = RequestMethod.POST)
	public TokenDTO authenticate(@RequestParam("username") final String username, @RequestParam("password") final String password) {
		return new TokenDTO(TokenUtils.createToken(userService.authenticate(username, password)));
	}

	@RequestMapping(value = USER_FIND_ADVERTS,
            method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Advert>> findUserAdverts(@PathVariable final Long userId){
		return ResponseEntity.ok().body(advertService.findByUserId(userId));
	}
}