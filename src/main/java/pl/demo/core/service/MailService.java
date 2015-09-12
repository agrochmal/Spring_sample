package pl.demo.core.service;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import pl.demo.web.dto.EMailDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by robertsikora on 27.07.15.
 */
@Validated
public interface MailService {
    void sendMail(@Valid EMailDTO emailDTO, @NotNull @NotBlank String template);
}
