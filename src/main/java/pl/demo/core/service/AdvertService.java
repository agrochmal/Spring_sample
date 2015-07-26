package pl.demo.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.Comment;
import pl.demo.web.dto.EMailDTO;
import pl.demo.web.dto.SearchCriteriaDTO;

import java.util.Collection;


public interface AdvertService extends CRUDService<Long, Advert> {

	Advert findOne(Long id);

	void delete(Long id);

	void edit(Advert advert);

	Advert save(Advert advert);

	Page<Advert> findAll(Pageable pageable);

	Collection<Advert> findByUserName();

	Advert createNew();

	Page<Advert> findBySearchCriteria(SearchCriteriaDTO searchCriteriaDTO, Pageable pageable);

	void updateActive(Long id, Boolean status);

	void sendMail(EMailDTO eMailDTO);

	void postComment(Long advertId, Comment comment);
}
