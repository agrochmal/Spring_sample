package pl.demo.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.apache.solr.analysis.StempelPolishStemFilterFactory;

import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.spatial.Coordinates;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name = "adverts")

@Spatial
@Indexed(index="Adverts")
@AnalyzerDef(name = "customanalyzer",
		tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
		filters = {
				@TokenFilterDef(factory = LowerCaseFilterFactory.class),
				@TokenFilterDef(factory = StempelPolishStemFilterFactory.class)
		})
public class Advert extends BaseEntity implements Serializable, Coordinates {

	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	@Length(max=80)

	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	@Analyzer(definition = "customanalyzer")

	@Column(length = 80, nullable = false)
	private String title;

	@Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
	@Analyzer(definition = "customanalyzer")

	@Lob
	@Column(name="description", columnDefinition="CLOB NOT NULL", table="adverts")
	private String description;

	@Column(nullable = false)
	private Boolean active = Boolean.TRUE;
	
	@NotEmpty
	@Length(max=80)
	@Column(length = 80, nullable = false)
	private String locationName;
	
	@Column(nullable = false)
	private Date creationDate = new Date();
	
	private Date endDate;

	@Length(max=80)

	@Column(length = 80)
	private String contact;
	
	@NotEmpty
	@Length(max=80)
	@Column(length = 80, nullable = false)
	private String phone;

	@Email
	@Length(max=80)
	@Column(length = 80)
	private String email;
	
	@NotNull

	@Column(nullable = false)
	private Double latitude=0d;
	
	@NotNull
	@Column(nullable = false)
	private Double longitude=0d;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
	private User user;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "comment_advert_id", nullable = false)
	private Set<Comment> comments = new HashSet<>();

	public Advert(){
	}

	public Advert(Long id, String title, String description, Boolean active, String locationName, Date endDate, String contact, Double latitude, Date creationDate, String phone, String email, Double longitude, User user) {
		this.id =id;
		this.title = title;
		this.description = description;
		this.active = active;
		this.locationName = locationName;
		this.endDate = endDate;
		this.contact = contact;
		this.latitude = latitude;
		this.creationDate = creationDate;
		this.phone = phone;
		this.email = email;
		this.longitude = longitude;
		this.user = user;
	}

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
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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

		if (active != null ? !active.equals(advert.active) : advert.active != null) return false;
		if (contact != null ? !contact.equals(advert.contact) : advert.contact != null) return false;
		if (creationDate != null ? !creationDate.equals(advert.creationDate) : advert.creationDate != null)
			return false;
		if (description != null ? !description.equals(advert.description) : advert.description != null) return false;
		if (email != null ? !email.equals(advert.email) : advert.email != null) return false;
		if (endDate != null ? !endDate.equals(advert.endDate) : advert.endDate != null) return false;
		if (latitude != null ? !latitude.equals(advert.latitude) : advert.latitude != null) return false;
		if (locationName != null ? !locationName.equals(advert.locationName) : advert.locationName != null)
			return false;
		if (longitude != null ? !longitude.equals(advert.longitude) : advert.longitude != null) return false;
		if (phone != null ? !phone.equals(advert.phone) : advert.phone != null) return false;
		if (title != null ? !title.equals(advert.title) : advert.title != null) return false;
		if (user != null ? !user.equals(advert.user) : advert.user != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = title != null ? title.hashCode() : 0;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (active != null ? active.hashCode() : 0);
		result = 31 * result + (locationName != null ? locationName.hashCode() : 0);
		result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
		result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
		result = 31 * result + (contact != null ? contact.hashCode() : 0);
		result = 31 * result + (phone != null ? phone.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
		result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
		result = 31 * result + (user != null ? user.hashCode() : 0);
		return result;
	}
}
