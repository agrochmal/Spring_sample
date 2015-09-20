package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pl.demo.core.aspects.DetachEntity;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.User;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.web.dto.EMailDTO;
import pl.demo.web.dto.SearchCriteriaDTO;

import java.util.Collection;
import java.util.Optional;

import static pl.demo.core.service.MailServiceImpl.EMAIL_TEMPLATE;

public class AdvertServiceImpl extends CRUDServiceImpl<Long, Advert>
		implements AdvertService {

	private SearchAdvertService searchService;
	private UserService userService;
	private MailService mailService;

	@Override
	protected Class<Advert> supportedDomainClass() {
		return Advert.class;
	}

	@Transactional
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
		return findBySearchCriteria(new SearchCriteriaDTO(), pageable);
	}

	@DetachEntity
	@Override
	public Collection<Advert> findByUserId(final Long userId){
		Assert.notNull(userId);
		return getAdvertRepository().findByUserId(userId);
	}

	@Override
	public Advert createNew() {
		return Optional.ofNullable(userService.getLoggedUser())
			.map(t-> createAdvert(t))
			.orElse(new Advert());
	}

	private Advert createAdvert(final User t){
		return Advert.AdvertBuilder.anAdvert()
				.withLocationName(t.getLocation())
				.withContact(t.getName())
				.withPhone(t.getPhone())
				.withEmail(t.getUsername())
				.withLatitude(t.getLat())
				.withLongitude(t.getLng()).build();
	}

	@DetachEntity
	@Override
	public Page<Advert> findBySearchCriteria(final SearchCriteriaDTO searchCriteriaDTO, final Pageable pageable) {
		final Page<Advert> adverts;
		if(searchCriteriaDTO.isEmpty()) {
			adverts = getAdvertRepository().findByActive(Boolean.TRUE, pageable);
		} else {
			adverts = searchService.searchAdverts(searchCriteriaDTO, pageable);
		}
		//adverts.getContent().stream().forEach(t -> detachAndUnproxyEntity(t));
		return adverts;
	}

	@Transactional
	@Override
	public void updateActiveStatus(final Long advertId, final Boolean status) {
		Assert.notNull(advertId, "Advert is required");
		final Advert dbAdvert = getDomainRepository().findOne(advertId);
		Assert.state(null != dbAdvert, "Advert doesn't exist in db");
		dbAdvert.setActive(status);
		super.save(dbAdvert);
	}

	@Transactional
	@Override
	public void sendMail(final EMailDTO eMailDTO) {
		Assert.notNull(eMailDTO, "Email data is required");
		this.mailService.sendMail(eMailDTO, EMAIL_TEMPLATE);
	}

	private AdvertRepository getAdvertRepository(){
		return (AdvertRepository)getDomainRepository();
	}

	@Autowired
	public void setSearchService(final SearchAdvertService searchService) {
		this.searchService = searchService;
	}

	@Autowired
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setMailService(final MailService mailService) {
		this.mailService = mailService;
	}
}
