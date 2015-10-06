package pl.demo.core.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.builder.ToStringBuilder;
import pl.demo.core.util.EntityUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity implements Serializable, FlatableEntity{

	@JsonIgnoreProperties(ignoreUnknown=true)
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	protected Long id;

	@Basic
	@Column(name="entry_date")
	private Date entryDate;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Date getEntryDate() {
		return new Date();
	}

	public void setEntryDate(Date entryDate) {
		if(entryDate != null) {
			this.entryDate = (Date) entryDate.clone();
		}
	}

	@Override
	public void flatEntity() {
		EntityUtils.setFieldValues(this, null, EntityUtils.ANNOTATIONS);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("entryDate", entryDate)
				.toString();
	}
}
