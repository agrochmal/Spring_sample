package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.aspects.DetachEntity;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.Comment;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.core.model.repo.CommentRepository;
import pl.demo.core.util.Assert;
import pl.demo.core.util.Utils;
import pl.demo.web.dto.EMailDTO;

import java.util.Collection;
import java.util.Date;

import static pl.demo.core.service.MailServiceImpl.COMMENT_TEMPLATE;

/**
 * Created by Robert on 22.02.15.
 */


public class CommentServiceImpl extends CRUDServiceImpl<Long, Comment> implements CommentService{

    private @Value("${comment.receipt-email}") String receipt_email;

    private MailService      mailService;
    private AdvertRepository advertRepository;

    @Transactional
    @Override
    public void postComment(final long advertId, final Comment comment) {
        Assert.notNull(advertId, "Advert id is required");
        Assert.notNull(comment, "Comment is required");
        prepareComment(comment);

        final Advert dbAdvert = advertRepository.findOne(advertId);
        Assert.notResourceFound(dbAdvert);
        comment.setAdvert(dbAdvert);
        getDomainRepository().save(comment);

        final float rate = getCommentRepository().findRateByAdvertId(advertId);
        dbAdvert.setRate(rate);
        advertRepository.save(dbAdvert);

        sendEmail(comment);
    }

    @Transactional(readOnly = true)
    @DetachEntity
    @Override
    public Collection<Comment> findByAdvert(final long advertId) {
        Assert.notNull(advertId, "Advert id is required");
        return getCommentRepository().findByAdvertIdOrderByDateDesc(advertId);
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

    private CommentRepository getCommentRepository(){
        return (CommentRepository)getDomainRepository();
    }

    @Autowired
    public void setMailService(final MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setAdvertRepository(final AdvertRepository advertRepository) {
        this.advertRepository = advertRepository;
    }
}
