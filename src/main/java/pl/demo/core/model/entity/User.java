package pl.demo.core.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.demo.core.model.entity.embeddable.Contact;
import pl.demo.core.model.entity.validation.UsernameUnique;
import pl.demo.core.model.entity.versioning.VersionableBaseEntity;
import pl.demo.core.service.registration.AccountStatus;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_100;
import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_80;

@Entity
@Table(name = "users")
public class User extends VersionableBaseEntity {

	@Length(max = TEXT_LENGTH_80)
    @Basic
	@Column(length = TEXT_LENGTH_80, nullable = false)
	private String password;

	@UsernameUnique(message = "Email should be unique!")
    @Basic
    @Column(nullable = false)
	private String name;

	@Valid
    @AttributeOverrides({
        @AttributeOverride(name = "email", column = @Column(length = TEXT_LENGTH_100, nullable = false, unique = true))}
    )
	@Embedded
	private Contact contact;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "id_user", referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(name = "id_role", referencedColumnName = "id") })
	private Set<Role> roles = new HashSet<>();

	@Enumerated(EnumType.STRING)
	@Column(name = "account_status")
	private AccountStatus accountStatus;

	private String salt;

	public User() {
	}

	public User(long id) {
		setId(id);
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

	@JsonProperty(value = "roles")
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

	@JsonIgnore
	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	@JsonIgnore
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
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
