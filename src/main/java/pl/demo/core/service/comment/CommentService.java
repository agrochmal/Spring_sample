package pl.demo.core.service.comment;

import org.springframework.validation.annotation.Validated;
import pl.demo.core.model.entity.Comment;
import pl.demo.core.service.basic_service.CRUDService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Created by robertsikora on 27.07.15.
 */

@Validated
public interface CommentService extends CRUDService<Long, Comment> {

    void postComment(@NotNull @Min(1) long advertId, @NotNull @Valid Comment comment);

    @NotNull @Valid
    Collection<Comment> findByAdvert(@NotNull @Min(1) long advertId);
}
