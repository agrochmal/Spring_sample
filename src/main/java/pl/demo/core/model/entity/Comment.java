package pl.demo.core.model.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_25;
import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_250;

/**
 * Created by Robert on 22.02.15.
 */

@Entity
@Table(name = "comments")
public class Comment extends BaseEntity implements Comparable<Comment> {

    private String nick;
    private String ipAddr;
    private Date dateCreated;
    private String text;
    private Advert advert;

    @Size(min=1, max=TEXT_LENGTH_250)
    @Column(nullable = false)
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Size(max=TEXT_LENGTH_25)
    @Column(name="ip_addr")
    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    @NotNull
    @Column(name="date_created", nullable = false)
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @NotEmpty
    @Lob
    @Column(name="text", columnDefinition="CLOB NOT NULL", table="comments")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="advert_id")
    public Advert getAdvert() {
        return advert;
    }

    public void setAdvert(Advert advert) {
        this.advert = advert;
    }

    @Override
    public int compareTo(Comment o) {
        return getDateCreated().compareTo(o.getDateCreated());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        return new EqualsBuilder()
                .append(nick, comment.nick)
                .append(ipAddr, comment.ipAddr)
                .append(dateCreated, comment.dateCreated)
                .append(text, comment.text)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(nick)
                .append(ipAddr)
                .append(dateCreated)
                .append(text)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("nick", nick)
                .append("ipAddr", ipAddr)
                .append("dateCreated", dateCreated)
                .append("text", text)
                .toString();
    }
}
