package pl.demo.core.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import pl.demo.core.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
	Role findByRoleName(@Param("name") String name);
}
