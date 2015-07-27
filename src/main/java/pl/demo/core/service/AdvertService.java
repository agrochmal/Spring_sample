package pl.demo.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.Comment;
import pl.demo.web.dto.EMailDTO;
import pl.demo.web.dto.SearchCriteriaDTO;

import java.util.Collection;


public interface AdvertService extends CRUDService<Long, Advert> {

	Page<Advert> findAll(Pageable pageable);

	Collection<Advert> findByUserName();

	Advert createNew();

	Page<Advert> findBySearchCriteria(SearchCriteriaDTO searchCriteriaDTO, Pageable pageable);

	void sendMail(EMailDTO eMailDTO);

	@Transactional(readOnly=false)
	void updateActive(Long id, Boolean status);

	@Transactional(readOnly=false)
	void postComment(Long advertId, Comment comment);
}
