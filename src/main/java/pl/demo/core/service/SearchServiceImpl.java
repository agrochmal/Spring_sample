package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.core.model.repo.fullTextSearch.AdvertSearchQueryBuilderImpl;
import pl.demo.web.dto.SearchCriteriaDTO;

/**
 * Created by Robert on 2014-12-03.
 */
@Service
public class SearchServiceImpl implements SearchService {

    private AdvertRepository advertRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<Advert> searchAdverts(final SearchCriteriaDTO searchCriteriaDTO, final Pageable pageable) {
        return advertRepository.search(new AdvertSearchQueryBuilderImpl(searchCriteriaDTO), pageable);
    }

    @Autowired
    public void setAdvertRepository(AdvertRepository advertRepository) {
        this.advertRepository = advertRepository;
    }
}
