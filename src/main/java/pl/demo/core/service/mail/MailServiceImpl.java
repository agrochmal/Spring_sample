package pl.demo.core.service.mail;

import com.google.common.base.Throwables;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    private @Value("${email.enable}") Boolean emailEnabled;
    private JavaMailSender                    mailSender;
    private VelocityEngine                    velocityEngine;

    @Override
    public void sendMail(final EMailDTO emailDTO, final Template template){
        if(!emailEnabled){
            LOGGER.info("Sending e-mails is disabled");
            return;
        }
        Assert.notNull(emailDTO, "Email data is required");
        Assert.notNull(template);
        final MimeMessage mimeMsg = this.mailSender.createMimeMessage();
        final Map<String, Object> model = new HashMap<>();
        model.put("userMessage", emailDTO);
        final String text = VelocityEngineUtils.mergeTemplateIntoString(this.velocityEngine, template.getValue(), model).replaceAll("\n", "<br>");

        final MimeMessageHelper helper = new MimeMessageHelper(mimeMsg);
        try{
            helper.setSubject(emailDTO.getTitle());
            helper.setTo(emailDTO.getReceipt());
            helper.setFrom(emailDTO.getSender());
            helper.setSentDate(new Date());
            helper.setText(text, true);
            this.mailSender.send(mimeMsg);

        } catch (final MessagingException e) {
            LOGGER.error("Error during sending email", e);
            Throwables.propagate(e);
        }
    }

    @Autowired
    public void setMailSender(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    public void setVelocityEngine(final VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
}
