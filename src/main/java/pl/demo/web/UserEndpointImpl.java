package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.User;
import pl.demo.core.service.advert.AdvertService;
import pl.demo.core.service.basic_service.CRUDService;

import java.util.Collection;


@RestController
public class UserEndpointImpl extends CRUDResourceImpl<Long, User> implements UserEndpoint {

	private AdvertService     advertService;

	@Override
	public ResponseEntity<Collection<Advert>> findUserAdverts(@PathVariable final Long userId){
		return ResponseEntity.ok().body(this.advertService.findByUserId(userId));
	}

	@Autowired
	@Qualifier("userService")
	public void setDomainService(final CRUDService domainService) {
		this.crudService = domainService;
	}

	@Autowired
	public void setAdvertService(final AdvertService advertService) {
		this.advertService = advertService;
	}
}