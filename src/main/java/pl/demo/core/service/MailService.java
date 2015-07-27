package pl.demo.core.service;

import pl.demo.web.dto.EMailDTO;

/**
 * Created by robertsikora on 27.07.15.
 */
public interface MailService {
    void sendMail(EMailDTO emailDTO);
}
