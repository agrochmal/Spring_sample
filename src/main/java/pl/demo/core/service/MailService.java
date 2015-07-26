package pl.demo.core.service;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MailService {

    private static final String TEMPLATE_PATH = "/velocity/message.vm";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Async
    public void sendMail(final EMailDTO emailDTO){
        Assert.notNull(emailDTO, "Email data is required");
        final MimeMessage mimeMsg = mailSender.createMimeMessage();
        final Map<String, Object> model = new HashMap<>();
        model.put("userMessage", emailDTO);
        final String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, TEMPLATE_PATH, model)
                .replaceAll("\n", "<br>");
        try{
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMsg);
            helper.setSubject(emailDTO.getTitle());
            helper.setTo(emailDTO.getReceipt());
            helper.setFrom(emailDTO.getSender());
            helper.setSentDate(new Date());
            helper.setText(text, true);
            mailSender.send(mimeMsg);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
