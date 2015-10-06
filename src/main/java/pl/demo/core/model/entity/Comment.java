package pl.demo.core.model.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.apache.solr.analysis.StempelPolishStemFilterFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.persistence.Index;
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

@Indexed(index="idx_comments")
@AnalyzerDef(name="comment_analyzer",
        tokenizer=@TokenizerDef(factory=StandardTokenizerFactory.class),
        filters={
                @TokenFilterDef(factory=LowerCaseFilterFactory.class),
                @TokenFilterDef(factory=StempelPolishStemFilterFactory.class)})
public class Comment extends BaseEntity {

    @Basic
    private String nick;

    @Basic
    @Size(max=TEXT_LENGTH_25)
    @Column(name="ip_addr")
    private String ipAddr;

    @Basic
    @Column(name="date", nullable = false)
    private Date date;

    @Field(index= org.hibernate.search.annotations.Index.YES, analyze=Analyze.YES, store=Store.NO)
    @Analyzer(definition = "comment_analyzer")
    @NotEmpty
    @Lob
    @Basic
    @Column(name="text", columnDefinition="CLOB NOT NULL", table="comments")
    private String text;

    @Min(1)
    @Max(10)
    @Basic
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

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public Date getDate() {
        return (Date) date.clone();
    }

    public void setDate(Date date) {
        this.date = (Date) date.clone();
    }

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
