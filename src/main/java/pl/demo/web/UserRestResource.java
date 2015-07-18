package pl.demo.web;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.demo.core.model.entity.AuthenticationUserDetails;
import pl.demo.web.dto.TokenDTO;
import pl.demo.web.dto.UserDTO;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.User;
import pl.demo.core.util.TokenUtils;
import pl.demo.core.service.UserService;
import pl.demo.core.service.AdvertService;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserRestResource extends AbstractCRUDResource<String, User>{

	@Autowired
	private AdvertService advertService;

	private UserService userService;

	@Autowired
	public UserRestResource(UserService userService) {
		super(userService);
		this.userService = userService;
	}

	@Override
	public ResponseEntity<?> editResource(@PathVariable String id,
										  @Valid @RequestBody User user,
										  @RequestHeader HttpHeaders headers,
										  BindingResult bindingResult){

		//Concurency access to common resource

		List<String> etgs = headers.getIfNoneMatch();
		if(etgs.size()>0){
			User currentUser = userService.findOne(user.getUsername());
			if(null != currentUser){
				int userHash = currentUser.hashCode();
				if(etgs.get(0).equals(String.valueOf(userHash)))
					return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
			}
		}

		return super.editResource(id, user, headers, bindingResult);
	}

	@Override
	public ResponseEntity<?> save(@Valid @RequestBody User user, BindingResult bindingResult) {
		try{
			if(null != userService.loadUserByUsername( user.getUsername() ) )
				bindingResult.reject("User already exist");
		}catch(UsernameNotFoundException ex){}

		return super.save(user, bindingResult);
	}

	@RequestMapping(value="/logged",
			method = RequestMethod.GET,
			produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<UserDTO> getLoggedUser() {
		final AuthenticationUserDetails user = userService.getLoggedUserDetails();
		if(null==user)
			throw new ResourceNotFoundException("User not found");

		return new ResponseEntity<>(new UserDTO(user.getUsername(),
									UserService.createRoleMap(user)),
									HttpStatus.OK );
	}

	@Override
	protected ResponseEntity<UserDTO> getResourceById(@PathVariable String id){

		return Optional.ofNullable(this.userService.getLoggedUser())
				.map(
						t -> {UserDTO dto = new UserDTO(t.getUsername(),
								t.getName(),
								t.getLocation(),
								t.getPhone(), UserService.createRoleMap(new AuthenticationUserDetails(t)));

							    return new ResponseEntity<>(dto, HttpStatus.OK );
						}
				)
				.orElseGet(() -> {
					throw new ResourceNotFoundException("User not found");
				});
	}

	@RequestMapping(value="/unique",
			method = RequestMethod.GET,
			produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<Boolean> checkUserUnique(String username) {

		UserDetails existing = null;
		try{
			existing = userService.loadUserByUsername( username);
		}catch(UsernameNotFoundException ex){}

		return Optional.ofNullable(existing)
				.map(user -> new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK))
				.orElse(new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK));
	}

	@RequestMapping(value="/authenticate",
			method = RequestMethod.POST)

	public TokenDTO authenticate(@RequestParam("username") String username, @RequestParam("password") String password) {

		return new TokenDTO(TokenUtils.createToken(userService.authenticate(username, password)));
	}

	@RequestMapping(value="{username}/adverts",
            method = RequestMethod.GET,
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<Collection<Advert>> findUserAdverts(@PathVariable String username){

		Collection<Advert> allEntries = advertService.findByUserName(username);
		allEntries.forEach(t->t.setUser(null));

		return new ResponseEntity<>(allEntries, HttpStatus.OK);
	}
}