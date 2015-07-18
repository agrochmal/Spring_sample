package pl.demo.core.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.model.entity.Comment;
import pl.demo.web.dto.EMailDTO;
import pl.demo.web.dto.SearchCriteriaDTO;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.core.model.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component("advertService")
@Transactional(readOnly = true)
public class AdvertServiceImpl implements AdvertService{

	@PersistenceContext
	private EntityManager entityManager;

	private final static int SHORT_DESCRIPTION_LENGTH = 255;

	private final AdvertRepository advertRepo;
	private final SearchService searchService;
	private final UserService userService;
	private final MailService mailService;


	@Autowired
	public AdvertServiceImpl(AdvertRepository advertRepo, SearchService searchService, UserService userService, MailService mailService){
		this.advertRepo = advertRepo;
		this.searchService = searchService;
		this.userService = userService;
		this.mailService = mailService;
	}

	@Override
	public Advert findOne(Long id) {

		Advert stored = advertRepo.findOne(id);
		return new Advert(stored.getId(), stored.getTitle(),stored.getDescription(),stored.getActive(),
				stored.getLocationName(), stored.getEndDate(), stored.getContact(), stored.getLatitude(), stored.getCreationDate(), stored.getPhone(), stored.getEmail(), stored.getLongitude(), null);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Long id) {
		this.advertRepo.delete(id);
	}

	@Override
	@Transactional(readOnly = false)
	public void edit(Advert advert) {
		throw new RuntimeException("Not supported yet");
	}

	@Override
	@Transactional(readOnly = false)
	public Advert save(Advert advert) {
		User loggedUser = userService.getLoggedUser();
		if(null != loggedUser)
			advert.setUser(loggedUser);
		return advertRepo.save(advert);
	}

	@Override
	public Page<Advert> findAll(Pageable pageable) {
		return findBySearchCriteria(null, pageable);
	}

	public Collection<Advert> findByUserName(String username){
		Long userId = userService.getLoggedUserDetails().getId();
		Collection<Advert> allEntries = advertRepo.findByUserId(userId);
		return allEntries;
	}

	public Advert createNew() {
		
		return Optional.ofNullable( userService.getLoggedUser() )
				.map(t->
				{
					Advert advert = new Advert();
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

	public Page<Advert> findBySearchCriteria(SearchCriteriaDTO searchCriteriaDTO, Pageable pageable) {

		Page<Advert> adverts;
		if(null== searchCriteriaDTO || searchCriteriaDTO.isEmpty())
			adverts = advertRepo.findByActive(Boolean.TRUE, pageable);
		else
			adverts =  searchService.select(searchCriteriaDTO, pageable);

		List<Advert> modified =	adverts.getContent()
				.stream()
				.map(t -> {
					String desc = t.getDescription();
					if (Objects.nonNull(desc) && desc.length() > SHORT_DESCRIPTION_LENGTH)
						desc = desc.substring(0, SHORT_DESCRIPTION_LENGTH).concat("...");
					return new
							Advert(t.getId(), t.getTitle(), desc,null, t.getLocationName(), null, null, null, t.getCreationDate(), null, null, null, null);
				}).collect(Collectors.toList());

		return new PageImpl(modified, pageable, adverts.getTotalElements());
	}

	@Transactional(readOnly = false)
	public void updateActive(Long id, Boolean status) {
		Advert advert = advertRepo.findOne(id);
		advert.setActive(status);
		advertRepo.save(advert);
	}

	public void sendMail(EMailDTO eMailDTO) {
		this.mailService.sendMail(eMailDTO);
	}

	@Transactional(readOnly = false)
	public void postComment(Long advertId, Comment comment){

		Advert advert = advertRepo.findOne(advertId);
		advert.addComment(comment);
		advertRepo.save(advert);
	}
}
