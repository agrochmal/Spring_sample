package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.User;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.web.dto.EMailDTO;
import pl.demo.web.dto.SearchCriteriaDTO;

import java.util.Collection;
import java.util.Optional;

import static pl.demo.core.service.MailServiceImpl.EMAIL_TEMPLATE;

@Component("advertService")
@Transactional(readOnly=true)
public class AdvertServiceImpl extends CRUDServiceImpl<Long, Advert>
		implements AdvertService {

	private final AdvertRepository advertRepo;
	private final SearchAdvertService searchService;
	private final UserService userService;
	private final MailService mailService;

	@Autowired
	public AdvertServiceImpl(final AdvertRepository advertRepo, final SearchAdvertService searchService,
							 final UserService userService, final MailService mailService){
		super(advertRepo);
		this.advertRepo = advertRepo;
		this.searchService = searchService;
		this.userService = userService;
		this.mailService = mailService;
	}

	@Override
	public Advert save(final Advert advert) {
		Assert.notNull(advert);
		final User loggedUser = userService.getLoggedUser();
		if(null != loggedUser) {
			advert.setUser(loggedUser);
		}
		return super.save(advert);
	}

	@Override
	public Page<Advert> findAll(final Pageable pageable) {
		Assert.notNull(pageable);
		return findBySearchCriteria(null, pageable);
	}

	@Override
	public Collection<Advert> findByUserId(final Long userId){
		Assert.notNull(userId);
		final Collection<Advert> adverts = advertRepo.findByUserId(userId);
		unproxyEntity(adverts);
		return adverts;
	}

	@Override
	public Advert createNew() {
		return Optional.ofNullable(userService.getLoggedUser())
			.map(t-> {
				final Advert advert = new Advert();
				advert.setLocationName(t.getLocation());
				advert.setContact(t.getName());
				advert.setPhone(t.getPhone());
				advert.setEmail(t.getUsername());
				advert.setLatitude(t.getLat());
				advert.setLongitude(t.getLng());
				return advert;
			})
			.orElse(new Advert());
	}

	@Override
	public Page<Advert> findBySearchCriteria(final SearchCriteriaDTO searchCriteriaDTO, final Pageable pageable) {
		final Page<Advert> adverts;
		if(null==searchCriteriaDTO || searchCriteriaDTO.isEmpty()) {
			adverts = advertRepo.findByActive(Boolean.TRUE, pageable);
		} else {
			adverts = searchService.searchAdverts(searchCriteriaDTO, pageable);
		}
		adverts.getContent().stream().forEach(t->unproxyEntity(t));
		return adverts;
	}

	@Override
	@Transactional(readOnly=false)
	public void updateActiveStatus(final Long advertId, final Boolean status) {
		Assert.notNull(advertId, "Advert is required");
		final Advert dbAdvert = advertRepo.findOne(advertId);
		Assert.state(null != dbAdvert, "Advert doesn't exist in db");
		dbAdvert.setActive(status);
		super.save(dbAdvert);
	}

	@Override
	public void sendMail(final EMailDTO eMailDTO) {
		Assert.notNull(eMailDTO, "Email data is required");
		this.mailService.sendMail(eMailDTO, EMAIL_TEMPLATE);
	}
}
