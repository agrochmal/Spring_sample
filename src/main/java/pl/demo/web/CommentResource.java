package pl.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.demo.core.model.entity.Comment;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.demo.web.EndpointConst.COMMENT.COMMENT_ENDPOINT;
import static pl.demo.web.EndpointConst.COMMENT.COMMENT_GETALL;
import static pl.demo.web.EndpointConst.COMMENT.COMMENT_NEW;

/**
 * Created by robertsikora on 11.10.15.
 */

@RequestMapping(COMMENT_ENDPOINT)
public interface CommentResource {

    @RequestMapping(value = COMMENT_NEW, method = RequestMethod.POST)
    ResponseEntity<?> postComment(@PathVariable("id") Long advertId, @Valid @RequestBody Comment comment,
                                  BindingResult bindingResult, HttpServletRequest httpServletRequest);

    @RequestMapping(value = COMMENT_GETALL, method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAllComments(@PathVariable("id") Long id);
}
