package pl.demo.core.service;

import pl.demo.core.model.entity.Comment;

import java.util.Collection;

/**
 * Created by robertsikora on 27.07.15.
 */
public interface CommentService extends CRUDService<Long, Comment> {

    void postComment(Long advertId, Comment comment);

    Collection<Comment> findByAdvert(Long advertId);
}
