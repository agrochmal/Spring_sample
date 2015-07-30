package pl.demo.core.model.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles")
@NamedQueries({
	@NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r"),
    @NamedQuery(name="Role.findByRoleName",
                query="SELECT r FROM Role r WHERE r.roleName.name = :name"),
}) 
public class Role extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @OneToOne
  private RoleName roleName;

  public Role() {}
  
  public Role(RoleName roleName) {
    this.roleName = roleName;
  }

  public RoleName getRoleName() {
    return roleName;
  }

  public void setRoleName(RoleName roleName) {
    this.roleName = roleName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    Role role = (Role) o;

    return new EqualsBuilder()
            .append(roleName, role.roleName)
            .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(roleName)
            .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
            .append("roleName", roleName)
            .toString();
  }
}
