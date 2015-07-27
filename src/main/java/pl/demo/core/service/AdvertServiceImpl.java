package pl.demo.core.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pl.demo.core.model.entity.Comment;
import pl.demo.core.model.repo.GenericRepository;
import pl.demo.web.dto.EMailDTO;
import pl.demo.web.dto.SearchCriteriaDTO;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.core.model.entity.User;

@Component("advertService")
@Transactional(readOnly=true)
public class AdvertServiceImpl implements AdvertService {

	private final static int SHORT_DESCRIPTION_LENGTH = 255;

	private final AdvertRepository advertRepo;
	private final GenericRepository<Advert> genericRepository;
	private final SearchService searchService;
	private final UserService userService;
	private final MailService mailService;

	@Autowired
	public AdvertServiceImpl(final AdvertRepository advertRepo, final GenericRepository<Advert> genericRepository, final SearchService searchService, final UserService userService, final MailService mailService){
		this.advertRepo = advertRepo;
		this.genericRepository = genericRepository;
		this.searchService = searchService;
		this.userService = userService;
		this.mailService = mailService;
	}

	@Override
	public Advert findOne(final Long id) {
		Assert.notNull(id, "Advert id is required");
		final Advert advert = advertRepo.findOne(id);
		Assert.state(null != advert, "Advert doesn't exist in db");
		genericRepository.detach(advert);
		advert.flatEntity();
		return advert;
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Long id) {
		Assert.notNull(id,  "Advert id is required");
		this.advertRepo.delete(id);
	}

	@Override
	@Transactional(readOnly = false)
	public void edit(final Advert advert) {
		throw new RuntimeException("Method is not supported yet");
	}

	@Override
	@Transactional(readOnly = false)
	public Advert save(final Advert advert) {
		Assert.notNull(advert, "Advert is required");
		final User loggedUser = userService.getLoggedUser();
		if(null != loggedUser) {
			advert.setUser(loggedUser);
		}
		return advertRepo.save(advert);
	}

	@Override
	public Page<Advert> findAll(final Pageable pageable) {
		return findBySearchCriteria(null, pageable);
	}

	public Collection<Advert> findByUserName(){
		final Long userId = userService.getLoggedUserDetails().getId();
		final Collection<Advert> allEntries = advertRepo.findByUserId(userId);
		return allEntries;
	}

	public Advert createNew() {
		return Optional.ofNullable( userService.getLoggedUser() )
				.map(t->
				{
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

	public Page<Advert> findBySearchCriteria(final SearchCriteriaDTO searchCriteriaDTO, final Pageable pageable) {

		final Page<Advert> adverts;
		if(null== searchCriteriaDTO || searchCriteriaDTO.isEmpty()) {
			adverts = advertRepo.findByActive(Boolean.TRUE, pageable);
		}
		else {
			adverts = searchService.select(searchCriteriaDTO, pageable);
		}

		final List<Advert> shortAdverts=adverts.getContent()
				.stream()
				.map(t -> {
					String desc = t.getDescription();
					if (Objects.nonNull(desc)
							&& desc.length() > SHORT_DESCRIPTION_LENGTH) {
						desc = desc.substring(0, SHORT_DESCRIPTION_LENGTH).concat("...");
					}
					genericRepository.detach(t);
					t.flatEntity();
					return t;
				}).collect(Collectors.toList());

		return new PageImpl(shortAdverts, pageable, adverts.getTotalElements());
	}

	@Transactional(readOnly = false)
	public void updateActive(final Long id, final Boolean status) {
		Assert.notNull(id, "Advert is required");
		final Advert advert = advertRepo.findOne(id);
		Assert.state(null != advert, "Advert doesn't exist in db");
		advert.setActive(status);
		advertRepo.save(advert);
	}

	public void sendMail(final EMailDTO eMailDTO) {
		Assert.notNull(eMailDTO, "Email data is required");
		this.mailService.sendMail(eMailDTO);
	}

	@Transactional(readOnly = false)
	public void postComment(final Long advertId, final Comment comment){
		Assert.notNull(advertId, "Advert is required");
		Assert.notNull(comment, "Comment is required");
		final Advert advert = advertRepo.findOne(advertId);
		Assert.state(null!=advert, "Advert doesn't exist in db");
		advert.addComment(comment);
		advertRepo.save(advert);
	}
}
