package pl.demo.core.service.mail.event;

import org.springframework.context.ApplicationEvent;
import pl.demo.core.events.BusinessEvent;
import pl.demo.core.service.mail.TemplateType;
import pl.demo.web.dto.EMail;

/**
 * Created by robertsikora on 22.10.15.
 */

public class SendMailEvent extends ApplicationEvent implements BusinessEvent<EMail> {

    private final TemplateType template;
    private boolean accepted = true;

    public SendMailEvent(final EMail source, final TemplateType template){
        super(source);
        this.template = template;
    }

    public EMail getTarget(){
        return (EMail) super.getSource();
    }

    public TemplateType getTemplate(){
        return template;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(final boolean accepted) {
        this.accepted = accepted;
    }
}
