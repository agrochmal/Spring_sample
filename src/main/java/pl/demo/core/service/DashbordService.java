package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.web.dto.DashbordDTO;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.core.model.repo.UserRepository;

/**
 * Created by Robert on 29.12.14.
 */

@Service
@Transactional(readOnly=true)
public class DashbordService {

    private final AdvertRepository advertRepo;
    private final UserRepository userRepo;

    @Autowired
    public DashbordService(final AdvertRepository advertRepo, final UserRepository userRepo) {
        this.advertRepo = advertRepo;
        this.userRepo = userRepo;
    }

    public DashbordDTO getData() {
        final long userCount = advertRepo.findByActive(Boolean.TRUE).size();
        final long advertCount = userRepo.count();
        return new DashbordDTO(userCount, advertCount);
    }
}
