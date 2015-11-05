package pl.demo.core.service.mail;

import org.springframework.context.ApplicationEvent;
import pl.demo.core.events.BusinessEvent;

/**
 * Created by robertsikora on 22.10.15.
 */

public class SendMailEvent extends ApplicationEvent implements BusinessEvent<pl.demo.web.dto.EMailDTO> {

    private final Template template;

    public SendMailEvent(final pl.demo.web.dto.EMailDTO source, final Template template){
        super(source);
        this.template = template;
    }

    public pl.demo.web.dto.EMailDTO getTarget(){
        return (pl.demo.web.dto.EMailDTO) super.getSource();
    }

    public Template getTemplate(){
        return template;
    }
}
