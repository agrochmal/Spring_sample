package pl.demo.core.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.builder.ToStringBuilder;
import pl.demo.core.model.entity.preprocessors.EnrichBaseEntityProcessor;
import pl.demo.core.util.EntityUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@EntityListeners(EnrichBaseEntityProcessor.class)
public abstract class BaseEntity implements Serializable, FlatableEntity {

	private final static Object NULL_VALUE = null;

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	protected Long id;

    @NotNull
	@Column(name = "entry_date", nullable = false)
	private Date entryDate = new Date();

	@Column(name = "entry_user")
	private String entryUser;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	@JsonIgnore
	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		if(entryDate != null) {
			this.entryDate = (Date) entryDate.clone();
		}
	}

	@JsonIgnore
	public String getEntryUser() {
		return entryUser;
	}

	public void setEntryUser(String entryUser) {
		this.entryUser = entryUser;
	}

	/*
	   Method makes flatten entity.
	   Thanks to it may be serialized and send over REST
	 */

	@Override
	public void flatEntity() {
		EntityUtils.setFieldValues(this, NULL_VALUE, EntityUtils.ANNOTATIONS);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id).toString();
	}
}
