package pl.demo.core.service.mail.event;

import org.springframework.context.ApplicationEvent;
import pl.demo.core.events.BusinessEvent;
import pl.demo.core.service.mail.Template;

/**
 * Created by robertsikora on 22.10.15.
 */

public class SendMailEvent extends ApplicationEvent implements BusinessEvent<pl.demo.web.dto.EMailDTO> {

    private final Template template;
    private boolean accepted = true;

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

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(final boolean accepted) {
        this.accepted = accepted;
    }
}
