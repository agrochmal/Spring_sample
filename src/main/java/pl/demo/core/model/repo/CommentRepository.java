package pl.demo.core.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.demo.core.model.entity.Comment;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Long>{

    Collection<Comment> findByAdvertIdOrderByDateDesc(Long advertId);

    @Query("SELECT AVG(c.rate) FROM Comment c WHERE c.advert.id=:id GROUP BY c.advert.id")
    Float findRateByAdvertId(@Param("id") Long id);

    @Query("SELECT AVG(c.rate) avg, c.advert.id id FROM Comment c GROUP BY c.advert.id ORDER BY avg")
    Collection<Object> findTop4();
}
