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
import pl.demo.core.util.Utils;
import pl.demo.web.dto.EMailDTO;

import java.util.Collection;
import java.util.Date;

import static pl.demo.core.service.MailServiceImpl.COMMENT_TEMPLATE;

/**
 * Created by Robert on 22.02.15.
 */

@Service
@Transactional(readOnly=true)
public class CommentServiceImpl extends CRUDServiceImpl<Long, Comment>
        implements CommentService{

    private @Value("${comment.receipt-email}") String receipt_email;

    private final MailService mailService;
    private final CommentRepository commentRepository;
    private final AdvertRepository advertRepository;

    @Autowired
    public CommentServiceImpl(final MailService mailService,
                              final CommentRepository commentRepository, final AdvertRepository advertRepository) {
        super(commentRepository);
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

        final float rate = commentRepository.findRateByAdvertId(advertId);
        dbAdvert.setRate(rate);
        advertRepository.save(dbAdvert);

        sendEmail(comment);
    }

    @Override
    public Collection<Comment> findByAdvert(Long advertId) {
        Assert.notNull(advertId, "Advert id is required");
        final Collection<Comment> comments = commentRepository.findByAdvertIdOrderByDateDesc(advertId);
        unproxyEntity(comments);
        return comments;
    }

    private void prepareComment(final Comment comment) {
        comment.setDate(new Date());
        comment.setText(Utils.escapeHtml(comment.getText()));
    }

    private void sendEmail(final Comment comment){
        final EMailDTO eMailDTO = new EMailDTO();
        eMailDTO.setTitle("Dodano nowy komentarz");
        eMailDTO.setContent(comment.getText());
        eMailDTO.setReceipt(receipt_email);
        eMailDTO.setSender(receipt_email);
        mailService.sendMail(eMailDTO, COMMENT_TEMPLATE);
    }
}
