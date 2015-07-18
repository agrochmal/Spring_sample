package pl.demo.core.service;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
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

    private static final String templatePath = "/velocity/message.vm";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Async
    public void sendMail(EMailDTO emailDTO){

        MimeMessage mimeMsg = mailSender.createMimeMessage();
        Map<String, Object> model = new HashMap<>();
        model.put("userMessage", emailDTO);
        String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templatePath, model);
        text = text.replaceAll("\n", "<br>");
        try{
            MimeMessageHelper helper = new MimeMessageHelper(mimeMsg);
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
