package pl.demo.core.service.mail;

import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import pl.demo.web.dto.EMailDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by robertsikora on 27.07.15.
 */

@Validated
public interface MailService {

    @Async(value="mailExecutor")
    void sendMail(@NotNull @Valid EMailDTO emailDTO, @NotNull Template template);
}
