package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.Comment;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.core.model.repo.CommentRepository;
import pl.demo.core.model.repo.fullTextSearch.queryBuilder.AdvertSearchQueryBuilderImpl;
import pl.demo.core.model.repo.fullTextSearch.queryBuilder.CommentSearchQueryBuiderImpl;
import pl.demo.web.dto.SearchCriteriaDTO;

import javax.validation.constraints.NotNull;

/**
 * Created by Robert on 2014-12-03.
 */
@Service
public class SearchServiceImpl implements SearchService {

    private AdvertRepository advertRepository;
    private CommentRepository commentRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<Advert> searchAdverts(final SearchCriteriaDTO searchCriteriaDTO, final Pageable pageable) {
        return advertRepository.search(new AdvertSearchQueryBuilderImpl(searchCriteriaDTO), pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Comment> searchComments(final SearchCriteriaDTO searchCriteriaDTO, final Pageable pageable) {
        return commentRepository.search(new CommentSearchQueryBuiderImpl(searchCriteriaDTO), pageable);
    }

    @Autowired
    public void setAdvertRepository(final AdvertRepository advertRepository) {
        this.advertRepository = advertRepository;
    }

    @Autowired
    public void setCommentRepository(final CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
}
