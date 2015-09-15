package pl.demo.core.service;

import org.springframework.validation.annotation.Validated;
import pl.demo.core.model.entity.Comment;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;

/**
 * Created by robertsikora on 27.07.15.
 */
@Validated
public interface CommentService extends CRUDService<Long, Comment> {

    void postComment(@Min(1) Long advertId, @Valid Comment comment);

    @Valid
    Collection<Comment> findByAdvert(@Min(1) Long advertId);
}
