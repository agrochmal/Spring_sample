package pl.demo.core.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.demo.core.model.entity.User;
import pl.demo.core.service.registration.AccountStatus;

public interface UserRepository extends JpaRepository<User, Long>{

	@Query("SELECT u FROM User u WHERE u.contact.email=:email and u.accountStatus=:accountStatus")
	User findByUsername(@Param("email") String email, @Param("accountStatus") AccountStatus accountStatus);

	@Query("SELECT u.salt FROM User u WHERE u.id=:id")
	String getUserSalt(@Param("id") Long id);

	@Modifying
	@Query("UPDATE User u SET u.salt=:salt WHERE u.id=:id")
	int updateUserSalt(@Param("salt") String salt, @Param("id") Long id);
}
