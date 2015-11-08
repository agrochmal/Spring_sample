package pl.demo.core.service.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.aspects.DetachEntity;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.Comment;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.core.model.repo.CommentRepository;
import pl.demo.core.service.basic_service.CRUDServiceImpl;
import pl.demo.core.service.mail.MailDTOSupplier;
import pl.demo.core.service.mail.Template;
import pl.demo.core.service.mail.event.SendMailEvent;
import pl.demo.core.util.Assert;
import pl.demo.core.util.Utils;
import pl.demo.core.util.WebUtils;

import java.util.Collection;
import java.util.Date;


/**
 * Created by Robert on 22.02.15.
 */


public class CommentServiceImpl extends CRUDServiceImpl<Long, Comment> implements CommentService{

    private AdvertRepository        advertRepository;

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

        assert comment != null;

        comment.setDate(new Date());
        comment.setText(Utils.escapeHtml(comment.getText()));
        comment.setIpAddr(WebUtils.getIpAdress(WebUtils.geHttpServletRequest()));
    }

    private void sendEmail(final Comment comment){

        assert comment != null;

        publishBusinessEvent(new SendMailEvent(MailDTOSupplier.get("Dodano nowy komentarz",
                comment.getText()).get(), Template.COMMENT_TEMPLATE));
    }

    private CommentRepository getCommentRepository(){
        return (CommentRepository)getDomainRepository();
    }

    @Autowired
    public void setAdvertRepository(final AdvertRepository advertRepository) {
        this.advertRepository = advertRepository;
    }
}
