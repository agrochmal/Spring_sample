package pl.demo.core.service.mail.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.demo.core.service.mail.MailService;

/**
 * Created by robertsikora on 22.10.15.
 */

@Component
public class MailSenderListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MailSenderListener.class);

    @Autowired
    private MailService mailService;

    @EventListener(condition = "#sendMailEvent.accepted==true")
    public void handleCreationEventAdvert(final SendMailEvent sendMailEvent) {
        LOGGER.debug("sending new e-mail to receipt {}", sendMailEvent.getTarget().getReceipt());
        mailService.sendMail(sendMailEvent.getTarget(), sendMailEvent.getTemplate());
    }
}
