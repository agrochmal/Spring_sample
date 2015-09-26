package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.core.model.repo.CommentRepository;
import pl.demo.core.model.repo.UserRepository;
import pl.demo.web.dto.DashboardDTO;

/**
 * Created by Robert on 29.12.14.
 */

@Service
public class DashboardServiceImpl implements DashboardService{

    private AdvertRepository advertRepository;
    private UserRepository userRepo;
    private CommentRepository commentRepository;

    @Transactional(readOnly = true)
    @Override
    public DashboardDTO buildDashboard() {
        return DashboardDTO.DashboardDTOBuilder.aDashboardDTO()
                .withAdverts(advertRepository.countByActive(Boolean.TRUE))
                .withComments(commentRepository.count())
                .withUsers(userRepo.count())
                .build();
    }

    @Autowired
    public void setAdvertRepository(final AdvertRepository advertRepository) {
        this.advertRepository = advertRepository;
    }

    @Autowired
    public void setUserRepo(final UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Autowired
    public void setCommentRepository(final CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
}
