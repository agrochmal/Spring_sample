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
@Transactional(readOnly = true)
public class DashbordService {

    private final AdvertRepository advertRepo;
    private final UserRepository userRepo;

    @Autowired
    public DashbordService(AdvertRepository advertRepo, UserRepository userRepo) {
        this.advertRepo = advertRepo;
        this.userRepo = userRepo;
    }

    public DashbordDTO getData() {

        long adverts = advertRepo.findByActive(Boolean.TRUE).size();
        long users = userRepo.count();

        return new DashbordDTO(users, adverts);
    }
}
