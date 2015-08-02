package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.core.model.repo.CommentRepository;
import pl.demo.core.model.repo.UserRepository;
import pl.demo.web.dto.DashboardDTO;

import java.util.Collection;

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
        final long userCount = userRepo.count();
        final long advertCount = advertRepository.countByActive(Boolean.TRUE);
        final long commentCount = commentRepository.count();
        return new DashboardDTO(userCount, advertCount, commentCount);
    }

    @Override
    public Collection<Advert> findTop4() {
        final Collection<Object> comments = commentRepository.findTop4();

        final Collection<Advert> adverts = advertRepository.findByAdvertIdIn(null);
        return adverts;
    }
}
