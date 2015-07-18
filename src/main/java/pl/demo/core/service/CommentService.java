package pl.demo.core.service;

import org.springframework.stereotype.Service;
import pl.demo.core.model.entity.Comment;
import pl.demo.core.util.PlainTextFilter;
import java.util.Date;

/**
 * Created by Robert on 22.02.15.
 */

@Service
public class CommentService {

   // @Inject private CommentMailSender mailSender;

    public void postComment(Comment comment) {

        prepareComment(comment);

       // callback.post(comment);
       // mailSender.sendNotificationEmail(comment);
    }

    private void prepareComment(Comment comment) {
        comment.setDateCreated(new Date());
        PlainTextFilter textFilter = new PlainTextFilter();
        comment.setText(textFilter.escapeHtml(comment.getText()));
    }
}
