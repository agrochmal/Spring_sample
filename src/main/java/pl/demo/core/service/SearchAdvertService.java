package pl.demo.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.demo.core.model.entity.Advert;
import pl.demo.web.dto.SearchCriteriaDTO;

/**
 * Created by robertsikora on 27.07.15.
 */
public interface SearchAdvertService {
    Page<Advert> searchAdverts(SearchCriteriaDTO searchCriteriaDTO, Pageable pageable);
}
