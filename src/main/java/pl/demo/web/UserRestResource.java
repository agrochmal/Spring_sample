package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
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

@RestController
@RequestMapping("/users")
public class UserRestResource extends AbstractCRUDResource<Long, User>{

	private final AdvertService advertService;
	private final UserService userService;

	@Autowired
	public UserRestResource(final UserService userService, final AdvertService advertService) {
		super(userService);
		this.userService = userService;
		this.advertService = advertService;
	}

	@RequestMapping(value="/logged",
			method = RequestMethod.GET,
			produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<UserDTO> getLoggedUser() {
		final AuthenticationUserDetails user = userService.getLoggedUserDetails();
		Assert.notNull(user);
		return new ResponseEntity<>(new UserDTO(user.getId(), user.getUsername(),
				UserService.createRoleMap(user)), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<UserDTO> getResourceById(@PathVariable Long id){
		return Optional.ofNullable(this.userService.getLoggedUser())
				.map(t -> {     final UserDTO dto = new UserDTO(t.getId(), t.getUsername(), t.getName(),
								t.getLocation(), t.getPhone(),
								UserService.createRoleMap(new AuthenticationUserDetails(t)));
							    return new ResponseEntity<>(dto, HttpStatus.OK );
						  }
				).orElseGet(() -> {
					throw new ResourceNotFoundException("User not found");
				});
	}

	@RequestMapping(value="/unique",
			method = RequestMethod.GET,
			produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<Boolean> checkUserUnique(final String username) {
		try {
			userService.loadUserByUsername(username);
		}catch (UsernameNotFoundException ex){
			return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
		}
		return new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);
	}

	@RequestMapping(value="/authenticate",
			method = RequestMethod.POST)
	public TokenDTO authenticate(@RequestParam("username") String username, @RequestParam("password") String password) {
		return new TokenDTO(TokenUtils.createToken(userService.authenticate(username, password)));
	}

	@RequestMapping(value="{userId}/adverts",
            method = RequestMethod.GET,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Advert>> findUserAdverts(@PathVariable Long userId){
		final Collection<Advert> allEntries = advertService.findByUserId(userId);
		return new ResponseEntity<>(allEntries, HttpStatus.OK);
	}
}