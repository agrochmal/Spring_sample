package pl.demo.core.model.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.demo.core.model.entity.embeddable.Contact;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_100;
import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_80;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

	@Length(max = TEXT_LENGTH_80)
    @Basic
	@Column(length = TEXT_LENGTH_80, nullable = false)
	private String password;

    @Basic
    @Column(nullable = false)
	private String name;

    @AttributeOverrides({
        @AttributeOverride(name = "email", column = @Column(length = TEXT_LENGTH_100, nullable = false, unique = true))}
    )
	@Embedded
	private Contact contact;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "id_user", referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(name = "id_role", referencedColumnName = "id") })
	private Set<Role> roles = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Advert> adverts = new HashSet<>();

	public User() {
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		roles.add(role);
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		final Set<Role> roles = getRoles();
		if (roles == null) {
			return Collections.emptyList();
		}
		return roles.stream().map( t-> new SimpleGrantedAuthority(t.getRoleName().getName())).collect(Collectors.toSet());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Set<Advert> getAdverts() {
		return adverts;
	}

	public void setAdverts(Set<Advert> adverts) {
		this.adverts = adverts;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		return new EqualsBuilder()
				.append(password, user.password)
				.append(name, user.name)
				.append(contact, user.contact)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(password)
				.append(name)
				.append(contact)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("password", password)
				.append("name", name)
				.append("contact", contact)
				.append("roles", roles)
				.toString();
	}
}
