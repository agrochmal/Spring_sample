package pl.demo.web.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_5_000;

/**
 * Created by Robert on 08.02.15.
 */
public class EMail {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EMail eMailDTO = (EMail) o;

        return new EqualsBuilder()
                .append(sender, eMailDTO.sender)
                .append(receipt, eMailDTO.receipt)
                .append(title, eMailDTO.title)
                .append(content, eMailDTO.content)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(sender)
                .append(receipt)
                .append(title)
                .append(content)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("sender", sender)
                .append("receipt", receipt)
                .append("title", title)
                .append("content", content)
                .toString();
    }
}
