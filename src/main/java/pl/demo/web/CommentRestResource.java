package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.demo.core.model.entity.Comment;
import pl.demo.core.service.CommentService;
import pl.demo.core.util.Assert;
import pl.demo.core.util.EntityUtils;
import pl.demo.core.util.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.demo.web.EndpointConst.COMMENT.*;

@RestController
@RequestMapping(COMMENT_ENDPOINT)
public class CommentRestResource extends AbstractCRUDResource<Long, Comment> {

    private final CommentService commentService;

    @Autowired
    public CommentRestResource(final CommentService commentService) {
        super(commentService);
        this.commentService = commentService;
    }

    @RequestMapping(value = COMMENT_NEW,
            method = RequestMethod.POST)

    public ResponseEntity<?> postComment(@PathVariable("id") final Long advertId, @Valid @RequestBody final Comment comment,
                             final BindingResult bindingResult, final HttpServletRequest httpServletRequest) {
        Assert.hasErrors(bindingResult);
        comment.setIpAddr(Utils.getIpAdress(httpServletRequest));
        this.commentService.postComment(advertId, comment);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = COMMENT_GETALL,
            method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)

    public ResponseEntity<?> getAllComments(@PathVariable("id") final Long id) {
        return ResponseEntity.ok().body(this.commentService.findByAdvert(id));
    }
}
