package pl.demo.core.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.demo.core.model.entity.RoleName;

public interface RoleNameRepository extends JpaRepository<RoleName, Long> {
}
