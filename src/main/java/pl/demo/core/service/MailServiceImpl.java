package pl.demo.core.service;

import com.google.common.base.Throwables;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.Assert;
import pl.demo.web.dto.EMailDTO;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert on 19.02.15.
 */

@Service
public class MailServiceImpl implements MailService{

    private final static Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    public static final String EMAIL_TEMPLATE = "/velocity/email_template.vm";
    public static final String COMMENT_TEMPLATE = "/velocity/comment_template.vm";

    private @Value("${mail.enable}") Boolean emailEnable;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Override
    @Async
    public void sendMail(final EMailDTO emailDTO, final String template){
        if(!emailEnable){
            return;
        }
        Assert.notNull(emailDTO, "Email data is required");
        Assert.notNull(template);
        final MimeMessage mimeMsg = mailSender.createMimeMessage();
        final Map<String, Object> model = new HashMap<>();
        model.put("userMessage", emailDTO);
        final String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, model).replaceAll("\n", "<br>");
        try{
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMsg);
            helper.setSubject(emailDTO.getTitle());
            helper.setTo(emailDTO.getReceipt());
            helper.setFrom(emailDTO.getSender());
            helper.setSentDate(new Date());
            helper.setText(text, true);
            mailSender.send(mimeMsg);

        } catch (MessagingException e) {
            logger.error("Error during sending email", e);
            Throwables.propagate(e);
        }
    }
}
