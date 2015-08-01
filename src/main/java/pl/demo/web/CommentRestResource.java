package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.demo.core.model.entity.Comment;
import pl.demo.core.service.CommentService;
import pl.demo.core.util.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/comments")
public class CommentRestResource extends AbstractCRUDResource<Long, Comment> {

    private final CommentService commentService;

    @Autowired
    public CommentRestResource(final CommentService commentService) {
        super(commentService);
        this.commentService = commentService;
    }

    @RequestMapping(value = "/advert/{id}",
            method = RequestMethod.POST)

    public ResponseEntity<?> postComment(@PathVariable("id") final Long id, @Valid @RequestBody final Comment comment,
                                         final BindingResult bindingResult, final HttpServletRequest httpServletRequest) {
        validateRequest(bindingResult);
        comment.setIpAddr(Utils.getIpAdress(httpServletRequest));
        commentService.postComment(id, comment);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/advert/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<?> getComments(@PathVariable("id") final Long id) {
        return ResponseEntity.ok()
                .body(this.commentService.findByAdvert(id));
    }
}
