package pl.demo.core.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.builder.ToStringBuilder;
import pl.demo.core.util.EntityUtils;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity implements FlatableEntity{

	@JsonIgnoreProperties(ignoreUnknown=true)
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.toString();
	}

	@Override
	public void flatEntity() {
		EntityUtils.setFieldValues(this, null, EntityUtils.ANNOTATIONS);
	}
}
