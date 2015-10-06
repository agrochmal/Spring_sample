package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pl.demo.core.aspects.DetachEntity;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.Comment;
import pl.demo.core.model.entity.MediaResource;
import pl.demo.core.model.entity.User;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.core.util.EntityUtils;
import pl.demo.web.HttpSessionContext;
import pl.demo.web.dto.EMailDTO;
import pl.demo.web.dto.SearchCriteriaDTO;
import java.io.IOException;
import java.util.*;

import static pl.demo.core.service.MailServiceImpl.EMAIL_TEMPLATE;

public class AdvertServiceImpl extends CRUDServiceImpl<Long, Advert> implements AdvertService {

	private final static Page<Advert> EMPTY_PAGE = new PageImpl(Collections.emptyList(), null, 0);

	private SearchService searchService;
	private UserService userService;
	private MailService mailService;
	private HttpSessionContext httpSessionContext;
	private ResourceMediaService resourceMediaService;

	@Transactional
	@Override
	public Advert save(final Advert advert) {
		Assert.notNull(advert);
		Advert saved;
		try {
			final User loggedUser = userService.getLoggedUser();
			if (null != loggedUser) {
				advert.setUser(loggedUser);
			}
			saved = super.save(advert);
			final Iterator<Long> idIterator = httpSessionContext.getUploadedResourcesId();
			while (idIterator.hasNext()) {
				final Long id = idIterator.next();
				final MediaResource mediaResource = resourceMediaService.findOne(id);
				mediaResource.setAdvert(saved);
				resourceMediaService.save(mediaResource);
			}
		}finally {
			try {
				httpSessionContext.close();
			} catch (IOException e) {
				LOGGER.error("", e);
			}
		}
		return saved;
	}

	@Transactional(readOnly = true)
	@DetachEntity
	@Override
	public Collection<Advert> findByUserId(final Long userId){
		Assert.notNull(userId);
		return getAdvertRepository().findByUserId(userId);
	}

	@Transactional(readOnly = true)
	@Override
	public Advert createNew() {
		return Optional.ofNullable(userService.getLoggedUser())
			.map(t-> createAdvert(t))
			.orElse(new Advert());
	}

	private Advert createAdvert(final User t){
		return Advert.AdvertBuilder.anAdvert()
				.withLocationName(t.getContact().getLocation().getLocation())
				.withContact(t.getName())
				.withPhone(t.getContact().getPhone())
				.withEmail(t.getContact().getEmail())
                .withLatitude(t.getContact().getLocation().getLat())
				.withLongitude(t.getContact().getLocation().getLng()).build();
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Advert> findAll(final Pageable pageable) {
		Assert.notNull(pageable);
		return findBySearchCriteria(new SearchCriteriaDTO(), pageable);
	}

	@Transactional(readOnly = true)
	@DetachEntity
	@Override
	public Page<Advert> findBySearchCriteria(final SearchCriteriaDTO searchCriteriaDTO, final Pageable pageable) {
		Page<Advert> adverts = EMPTY_PAGE;
		if(searchCriteriaDTO.isEmpty()) {
			adverts = getAdvertRepository().findByActive(Boolean.TRUE, pageable);
		} else {
			if(searchCriteriaDTO.isAdvertSearchMode()) {
				adverts = searchService.searchAdverts(searchCriteriaDTO, pageable);
			}else if(searchCriteriaDTO.isCommentSearchMode()){
				final Page<Comment> comments = searchService.searchComments(searchCriteriaDTO, pageable);
				if(comments.hasContent()){
					final List<Advert> advertList = new ArrayList<>();
					comments.getContent().forEach(c->advertList.add((Advert)EntityUtils.initializeHibernateEntity(c.getAdvert())));
					adverts = new PageImpl(advertList, pageable, advertList.size());
				}
			}else {
				Assert.state(false, "The search mode is not set.");
			}
		}
		adverts.getContent().forEach(t-> t.setThumbUrl(resourceMediaService.getThumb(t.getId())));
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

	@Override
	public void sendMail(final EMailDTO eMailDTO) {
		Assert.notNull(eMailDTO, "Email data is required");
		this.mailService.sendMail(eMailDTO, EMAIL_TEMPLATE);
	}

	private AdvertRepository getAdvertRepository(){
		return (AdvertRepository)getDomainRepository();
	}

	@Autowired
	public void setSearchService(final SearchService searchService) {
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

	@Autowired
	public void setHttpSessionContext(final HttpSessionContext httpSessionContext) {
		this.httpSessionContext = httpSessionContext;
	}

	@Autowired
	public void setResourceMediaService(final ResourceMediaService resourceMediaService) {
		this.resourceMediaService = resourceMediaService;
	}
}
