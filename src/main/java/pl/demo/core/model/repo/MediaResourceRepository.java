package pl.demo.core.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.demo.core.model.entity.MediaResource;

public interface MediaResourceRepository extends JpaRepository<MediaResource, Long>{

}

