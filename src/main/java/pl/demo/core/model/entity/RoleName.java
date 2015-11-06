package pl.demo.core.model.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_80;

@Entity
@Table(name = "role_names")
public class RoleName extends BaseEntity {

  public final static String USER_ROLE = "USER";
  public final static String ADMIN_ROLE = "ADMIN";

  @Basic
  @Column(length = TEXT_LENGTH_80, unique=true)
  private String name;
  
  public RoleName() {}
  
  public RoleName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    RoleName roleName = (RoleName) o;

    return new EqualsBuilder()
            .append(name, roleName.name)
            .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(name)
            .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
            .append("name", name)
            .toString();
  }
}
