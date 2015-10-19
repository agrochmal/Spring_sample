package pl.demo.core.model.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.apache.solr.analysis.StempelPolishStemFilterFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.spatial.Coordinates;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import pl.demo.core.model.entity.embeddable.Contact;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_80;

@Entity
@Table(name = "adverts")

@Spatial
@Indexed(index="idx_adverts")
@AnalyzerDef(name="advert_analyzer",
		tokenizer=@TokenizerDef(factory=StandardTokenizerFactory.class),
		filters={
				@TokenFilterDef(factory=LowerCaseFilterFactory.class),
				@TokenFilterDef(factory=StempelPolishStemFilterFactory.class)})
public class Advert extends BaseEntity implements Coordinates {

    @NotEmpty
    @Length(max = TEXT_LENGTH_80)
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Analyzer(definition = "advert_analyzer")
    @Basic
    @Column(length = TEXT_LENGTH_80, nullable = false)
    private String title;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Analyzer(definition = "advert_analyzer")
    @Lob
    @Basic
    @Column(name = "description", columnDefinition = "TEXT NOT NULL", table = "adverts")
    private String description;

    @Basic
    @Column(nullable = false)
    private Boolean active = Boolean.TRUE;

    @NotNull
    @Basic
    @Column(nullable = false)
    private Date creationDate = new Date();

    @Basic
    private Date endDate;

    @Length(max = TEXT_LENGTH_80)
    @Basic
    @Column(name = "owner_name", length = TEXT_LENGTH_80)
    private String ownerName;

    @Basic
    private Float rate = 1.0f;

    @Basic
    private String thumbUrl;

    @Embedded
    private Contact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "advert")
    private Set<Comment> comments = new HashSet<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndDate() {
        if (endDate != null) {
            return (Date) endDate.clone();
        }
        return null;
    }

    public void setEndDate(Date endDate) {
        if (endDate != null) {
            this.endDate = (Date) endDate.clone();
        }
    }

    public Date getCreationDate() {
        if (creationDate != null) {
            return (Date) creationDate.clone();
        }
        return null;
    }

    public void setCreationDate(Date creationDate) {
        if (creationDate != null) {
            this.creationDate = (Date) creationDate.clone();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean isActive) {
        this.active = isActive;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    @Transient
    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }


    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public Double getLatitude() {
        if(this.getContact() == null){
            return null;
        }
        return this.getContact().getLocation().getLat();
    }

    @Override
    public Double getLongitude() {
        if(this.getContact() == null){
            return null;
        }
        return this.getContact().getLocation().getLng();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Advert advert = (Advert) o;

        return new EqualsBuilder()
                .append(title, advert.title)
                .append(description, advert.description)
                .append(active, advert.active)
                .append(creationDate, advert.creationDate)
                .append(endDate, advert.endDate)
                .append(ownerName, advert.ownerName)
                .append(rate, advert.rate)
                .append(thumbUrl, advert.thumbUrl)
                .append(contact, advert.contact)
                .append(user, advert.user)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(title)
                .append(description)
                .append(active)
                .append(creationDate)
                .append(endDate)
                .append(ownerName)
                .append(rate)
                .append(thumbUrl)
                .append(contact)
                .append(user)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", title)
                .append("description", description)
                .append("active", active)
                .append("creationDate", creationDate)
                .append("endDate", endDate)
                .append("ownerName", ownerName)
                .append("rate", rate)
                .append("thumbUrl", thumbUrl)
                .append("contact", contact)
                .append("user", user)
                .append("comments", comments)
                .toString();
    }

    public static class AdvertBuilder {
        protected Long id;
        private String title;
        private String description;
        private Boolean active = Boolean.TRUE;

        private Date creationDate = new Date();
        private Date endDate;
        private String ownerName;
        private Float rate = 1.0f;
        private String thumbUrl;
        private Contact contact;
        private User user;
        private Set<Comment> comments = new HashSet<>();
        private Date entryDate;

        private AdvertBuilder() {
        }

        public static AdvertBuilder anAdvert() {
            return new AdvertBuilder();
        }

        public AdvertBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public AdvertBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public AdvertBuilder withActive(Boolean active) {
            this.active = active;
            return this;
        }

        public AdvertBuilder withCreationDate(Date creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public AdvertBuilder withEndDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public AdvertBuilder withOwnerName(String ownerName) {
            this.ownerName = ownerName;
            return this;
        }

        public AdvertBuilder withRate(Float rate) {
            this.rate = rate;
            return this;
        }

        public AdvertBuilder withThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
            return this;
        }

        public AdvertBuilder withContact(Contact contact) {
            this.contact = contact;
            return this;
        }

        public AdvertBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public AdvertBuilder withComments(Set<Comment> comments) {
            this.comments = comments;
            return this;
        }

        public AdvertBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public AdvertBuilder withEntryDate(Date entryDate) {
            this.entryDate = entryDate;
            return this;
        }

        public AdvertBuilder but() {
            return anAdvert().withTitle(title).withDescription(description).withActive(active).withCreationDate(creationDate).withEndDate(endDate).withOwnerName(ownerName).withRate(rate).withThumbUrl(thumbUrl).withContact(contact).withUser(user).withComments(comments).withId(id).withEntryDate(entryDate);
        }

        public Advert build() {
            Advert advert = new Advert();
            advert.setTitle(title);
            advert.setDescription(description);
            advert.setActive(active);
            advert.setCreationDate(creationDate);
            advert.setEndDate(endDate);
            advert.setOwnerName(ownerName);
            advert.setRate(rate);
            advert.setThumbUrl(thumbUrl);
            advert.setContact(contact);
            advert.setUser(user);
            advert.setComments(comments);
            advert.setId(id);
            return advert;
        }
    }
}


