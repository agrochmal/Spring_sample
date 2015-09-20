package pl.demo.core.service;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pl.demo.web.dto.EMailDTO;

import javax.validation.Valid;

/**
 * Created by robertsikora on 27.07.15.
 */

@Transactional(readOnly = true)
@Validated
public interface MailService {
    @Async(value="mailExecutor")
    void sendMail(@Valid EMailDTO emailDTO, @NotBlank String template);
}
