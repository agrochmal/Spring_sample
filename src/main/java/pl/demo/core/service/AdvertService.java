package pl.demo.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.Comment;
import pl.demo.web.dto.EMailDTO;
import pl.demo.web.dto.SearchCriteriaDTO;

import java.util.Collection;


public interface AdvertService extends CRUDService<Long, Advert> {

	public Advert findOne(Long id);

	public void delete(Long id);

	public void edit(Advert advert);

	public Advert save(Advert advert);

	public Page<Advert> findAll(Pageable pageable);

	public Collection<Advert> findByUserName(String username);

	public Advert createNew();

	public Page<Advert> findBySearchCriteria(SearchCriteriaDTO searchCriteriaDTO, Pageable pageable);

	public void updateActive(Long id, Boolean status);

	public void sendMail(EMailDTO eMailDTO);

	public void postComment(Long advertId, Comment comment);
}
