package pl.demo.web.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_5_000;

/**
 * Created by Robert on 08.02.15.
 */
public class EMailDTO {

    @NotEmpty
    @Email
    private String sender;

    @NotEmpty
    @Email
    private String receipt;

    @NotEmpty
    private String title;

    @NotEmpty
    @Size(max=TEXT_LENGTH_5_000)
    private String content;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
