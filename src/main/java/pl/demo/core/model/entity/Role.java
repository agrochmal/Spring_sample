package pl.demo.core.model.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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

}
