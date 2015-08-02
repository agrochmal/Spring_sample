package pl.demo.core.model.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;

import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_25;

/**
 * Created by Robert on 22.02.15.
 */

@Entity
@Table(name="comments")
public class Comment extends BaseEntity {

    private String nick;
    private String ipAddr;
    private Date date;
    private String text;
    private Integer rate;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="advert_id")
    private Advert advert;

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

    @Column(name="date", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Advert getAdvert() {
        return advert;
    }

    public void setAdvert(Advert advert) {
        this.advert = advert;
    }

    @Min(1)
    @Max(10)
    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        return new EqualsBuilder()
                .append(nick, comment.nick)
                .append(ipAddr, comment.ipAddr)
                .append(date, comment.date)
                .append(text, comment.text)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(nick)
                .append(ipAddr)
                .append(date)
                .append(text)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("nick", nick)
                .append("ipAddr", ipAddr)
                .append("dateCreated", date)
                .append("text", text)
                .toString();
    }
}
