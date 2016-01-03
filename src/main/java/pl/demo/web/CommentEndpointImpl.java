package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.demo.core.model.entity.Comment;
import pl.demo.core.service.basicservice.CRUDService;
import pl.demo.core.service.comment.CommentService;
import pl.demo.core.util.Assert;

import javax.validation.Valid;

@RestController
public class CommentEndpointImpl extends CRUDResourceImpl<Long, Comment> implements CommentEndpoint {

    @Override
    public ResponseEntity<?> postComment(@PathVariable("id") final Long advertId, @Valid @RequestBody final Comment comment,
                             final BindingResult bindingResult) {
        Assert.hasErrors(bindingResult);

        this.getCommentService().postComment(advertId, comment);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> getAllComments(@PathVariable("id") final Long id) {

        return ResponseEntity.ok().body(this.getCommentService().findByAdvert(id));
    }

    private CommentService getCommentService(){
        return (CommentService)crudService;
    }

    @Autowired
    @Qualifier("commentService")
    public void setDomainService(final CRUDService domainService) {
        this.crudService = domainService;
    }
}
