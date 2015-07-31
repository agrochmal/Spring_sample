package pl.demo.core.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.demo.core.model.entity.Comment;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    Collection<Comment> findByAdvert(Long advertId);
}
