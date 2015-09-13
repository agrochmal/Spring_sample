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
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_80;

@Entity
@Table(name = "adverts")

@Spatial
@Indexed(index="Adverts")
@AnalyzerDef(name="customanalyzer",
		tokenizer=@TokenizerDef(factory=StandardTokenizerFactory.class),
		filters={
				@TokenFilterDef(factory=LowerCaseFilterFactory.class),
				@TokenFilterDef(factory=StempelPolishStemFilterFactory.class)
		})
public class Advert extends BaseEntity implements Searchable, Coordinates {
	
	@NotEmpty
	@Length(max=TEXT_LENGTH_80)

	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	@Analyzer(definition = "customanalyzer")

	@Column(length=TEXT_LENGTH_80, nullable=false)
	private String title;

	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	@Analyzer(definition = "customanalyzer")

	@Lob
	@Column(name="description", columnDefinition="TEXT NOT NULL", table="adverts")
	private String description;

	@Column(nullable=false)
	private Boolean active = Boolean.TRUE;
	
	@NotEmpty
	@Length(max=TEXT_LENGTH_80)
	@Column(length=TEXT_LENGTH_80, nullable=false)
	private String locationName;
	
	@Column(nullable = false)
	private Date creationDate = new Date();
	
	private Date endDate;

	@Length(max=TEXT_LENGTH_80)

	@Column(length=TEXT_LENGTH_80)
	private String contact;
	
	@NotEmpty
	@Length(max=TEXT_LENGTH_80)
	@Column(length=TEXT_LENGTH_80, nullable = false)
	private String phone;

	@Email
	@Length(max=TEXT_LENGTH_80)
	@Column(length=TEXT_LENGTH_80)
	private String email;
	
	@NotNull

	@Column(nullable=false)
	private Double latitude=0d;
	
	@NotNull
	@Column(nullable=false)
	private Double longitude=0d;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
	private User user;

	private Float rate=1.0f;

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
		return (Date) endDate.clone();
	}

	public void setEndDate(Date endDate) {
		this.endDate = (Date)endDate.clone();
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreationDate() {
		return (Date) creationDate.clone();
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = (Date)creationDate.clone();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
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

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public void addComment(Comment comment){
		this.comments.add(comment);
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
				.append(locationName, advert.locationName)
				.append(creationDate, advert.creationDate)
				.append(endDate, advert.endDate)
				.append(contact, advert.contact)
				.append(phone, advert.phone)
				.append(email, advert.email)
				.append(latitude, advert.latitude)
				.append(longitude, advert.longitude)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(title)
				.append(description)
				.append(active)
				.append(locationName)
				.append(creationDate)
				.append(endDate)
				.append(contact)
				.append(phone)
				.append(email)
				.append(latitude)
				.append(longitude)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("title", title)
				.append("description", description)
				.append("active", active)
				.append("locationName", locationName)
				.append("creationDate", creationDate)
				.append("endDate", endDate)
				.append("contact", contact)
				.append("phone", phone)
				.append("email", email)
				.append("latitude", latitude)
				.append("longitude", longitude)
				.toString();
	}


	public static class AdvertBuilder {
		protected Long id;

		private String title;

		private String description;
		private Boolean active = Boolean.TRUE;
		private String locationName;
		private Date creationDate = new Date();
		private Date endDate;

		private String contact;
		private String phone;
		private String email;

		private Double latitude=0d;
		private Double longitude=0d;
		private User user;
		private Float rate=1.0f;
		private Set<Comment> comments = new HashSet<>();

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

		public AdvertBuilder withLocationName(String locationName) {
			this.locationName = locationName;
			return this;
		}

		public AdvertBuilder withCreationDate(Date creationDate) {
			this.creationDate = (Date)creationDate.clone();
			return this;
		}

		public AdvertBuilder withEndDate(Date endDate) {
			this.endDate = (Date)endDate.clone();
			return this;
		}

		public AdvertBuilder withContact(String contact) {
			this.contact = contact;
			return this;
		}

		public AdvertBuilder withPhone(String phone) {
			this.phone = phone;
			return this;
		}

		public AdvertBuilder withEmail(String email) {
			this.email = email;
			return this;
		}

		public AdvertBuilder withLatitude(Double latitude) {
			this.latitude = latitude;
			return this;
		}

		public AdvertBuilder withLongitude(Double longitude) {
			this.longitude = longitude;
			return this;
		}

		public AdvertBuilder withUser(User user) {
			this.user = user;
			return this;
		}

		public AdvertBuilder withRate(Float rate) {
			this.rate = rate;
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

		public AdvertBuilder but() {
			return anAdvert().withTitle(title).withDescription(description).withActive(active).withLocationName(locationName).withCreationDate(creationDate).withEndDate(endDate).withContact(contact).withPhone(phone).withEmail(email).withLatitude(latitude).withLongitude(longitude).withUser(user).withRate(rate).withComments(comments).withId(id);
		}

		public Advert build() {
			Advert advert = new Advert();
			advert.setTitle(title);
			advert.setDescription(description);
			advert.setActive(active);
			advert.setLocationName(locationName);
			advert.setCreationDate(creationDate);
			advert.setEndDate(endDate);
			advert.setContact(contact);
			advert.setPhone(phone);
			advert.setEmail(email);
			advert.setLatitude(latitude);
			advert.setLongitude(longitude);
			advert.setUser(user);
			advert.setRate(rate);
			advert.setComments(comments);
			advert.setId(id);
			return advert;
		}
	}
}
