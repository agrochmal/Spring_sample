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
@Transactional(readOnly=true)
public class DashboardServiceImpl implements DashboardService{

    private final AdvertRepository advertRepository;
    private final UserRepository userRepo;
    private final CommentRepository commentRepository;

    @Autowired
    public DashboardServiceImpl(final AdvertRepository advertRepo, final UserRepository userRepo,
                                final CommentRepository commentRepository) {
        this.advertRepository = advertRepo;
        this.userRepo = userRepo;
        this.commentRepository = commentRepository;
    }

    @Override
    public DashboardDTO buildDashboard() {
        return DashboardDTO.DashboardDTOBuilder.aDashboardDTO()
                .withAdverts(advertRepository.countByActive(Boolean.TRUE))
                .withComments(commentRepository.count())
                .withUsers(userRepo.count())
                .build();
    }
}
