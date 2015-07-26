package pl.demo.core.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_80;

@Entity
@Table(name = "role_names")
public class RoleName extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(length=TEXT_LENGTH_80, unique=true)
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
  public String toString() {
    return name;
  }
  
}
