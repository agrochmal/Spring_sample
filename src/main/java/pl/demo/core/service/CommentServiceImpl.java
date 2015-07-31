package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.Comment;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.core.model.repo.CommentRepository;
import pl.demo.core.util.PlainTextFilter;
import pl.demo.web.dto.EMailDTO;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Robert on 22.02.15.
 */

@Service
@Transactional(readOnly=true)
public class CommentServiceImpl extends CRUDServiceImpl<Long, Comment>
        implements CommentService{

    private final static String SENDER_EMAIL="demo";
    private @Value("${comment.receipt-email}") String receipt_email;

    private final PlainTextFilter plainTextFilter;
    private final MailService mailService;
    private final CommentRepository commentRepository;
    private final AdvertRepository advertRepository;

    @Autowired
    public CommentServiceImpl(final PlainTextFilter plainTextFilter, final MailService mailService,
                              final CommentRepository commentRepository, final AdvertRepository advertRepository) {
        super(commentRepository);
        this.plainTextFilter = plainTextFilter;
        this.mailService = mailService;
        this.commentRepository = commentRepository;
        this.advertRepository = advertRepository;
    }

    @Override
    @Transactional(readOnly=false)
    public void postComment(final Long advertId, final Comment comment) {
        Assert.notNull(advertId, "Advert id is required");
        Assert.notNull(comment, "Comment is required");
        prepareComment(comment);

        final Advert dbAdvert = advertRepository.findOne(advertId);
        Assert.state(null != dbAdvert);
        comment.setAdvert(dbAdvert);
        getJpaRepository().save(comment);

        final EMailDTO eMailDTO = new EMailDTO();
        eMailDTO.setTitle("New comment added");
        eMailDTO.setContent(comment.getText());
        eMailDTO.setReceipt(receipt_email);
        eMailDTO.setSender(SENDER_EMAIL);
        mailService.sendMail(eMailDTO);
    }

    @Override
    public Collection<Comment> findByAdvert(Long advertId) {
        Assert.notNull(advertId, "Advert id is required");
        return commentRepository.findByAdvert(advertId);
    }

    private void prepareComment(final Comment comment) {
        comment.setDateCreated(new Date());
        comment.setText(plainTextFilter.escapeHtml(comment.getText()));
    }
}
