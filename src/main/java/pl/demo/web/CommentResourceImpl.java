package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.demo.core.model.entity.Comment;
import pl.demo.core.service.CRUDService;
import pl.demo.core.service.CommentService;
import pl.demo.core.util.Assert;
import pl.demo.core.util.Utils;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class CommentResourceImpl extends CRUDResourceImpl<Long, Comment> implements CommentResource {

    @Override
    public ResponseEntity<?> postComment(@PathVariable("id") final Long advertId, @Valid @RequestBody final Comment comment,
                             final BindingResult bindingResult, final HttpServletRequest httpServletRequest) {
        Assert.hasErrors(bindingResult);
        comment.setIpAddr(Utils.getIpAdress(httpServletRequest));
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
