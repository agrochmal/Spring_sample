package pl.demo.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.demo.core.model.entity.Advert;
import pl.demo.web.dto.EMailDTO;
import pl.demo.web.dto.SearchCriteriaDTO;

import java.util.Collection;


public interface AdvertService extends CRUDService<Long, Advert> {

	Page<Advert> findAll(Pageable pageable);

	Collection<Advert> findByUserId(Long id);

	Page<Advert> findBySearchCriteria(SearchCriteriaDTO searchCriteriaDTO, Pageable pageable);

	Advert createNew();

	void sendMail(EMailDTO eMailDTO);

	void updateActiveStatus(Long id, Boolean status);
}
