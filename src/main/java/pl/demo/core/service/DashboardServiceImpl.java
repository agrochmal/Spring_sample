package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.core.model.repo.UserRepository;
import pl.demo.web.dto.DashboardDTO;

/**
 * Created by Robert on 29.12.14.
 */

@Service
@Transactional(readOnly=true)
public class DashboardServiceImpl implements DashboardService{

    private final AdvertRepository advertRepo;
    private final UserRepository userRepo;

    @Autowired
    public DashboardServiceImpl(final AdvertRepository advertRepo, final UserRepository userRepo) {
        this.advertRepo = advertRepo;
        this.userRepo = userRepo;
    }

    @Override
    public DashboardDTO buildDashboard() {
        final long userCount = userRepo.count();
        final long advertCount = advertRepo.countByActive(Boolean.TRUE);
        final long commentCount = 0;
        return new DashboardDTO(userCount, advertCount, commentCount);
    }
}
