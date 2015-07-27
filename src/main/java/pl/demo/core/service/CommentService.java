package pl.demo.core.service;

import pl.demo.core.model.entity.Comment;

/**
 * Created by robertsikora on 27.07.15.
 */
public interface CommentService {
    void postComment(Comment comment);
}
