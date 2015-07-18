package pl.demo.core.model.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "users")
public class User extends BaseEntity{

	@NotNull
	@Length(max=80)

	@Column(unique = true, length = 80, nullable = false)
	private String username;

	@Length(max=80)

	@Column(length = 80, nullable = false)
	private String password;

	private String name;

	@NotNull
	@Column(nullable = false)
	private String location;

	@NotNull

	@Column(nullable = false)
	private String phone;

	@Column(nullable = false)
	private Double lat = 0d;

	@Column(nullable = false)
	private Double lng = 0d;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "id_user", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "id_role", referencedColumnName = "id") })
	private Set<Role> roles = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Advert> adverts = new HashSet<>();

	public User() {
	}

	public User(String username, String passwordHash) {
		this.setUsername(username);
		this.setPassword(passwordHash);
	}

	public User(String username, String name, String location, String phone) {
		this.setUsername(username);
		this.setName(name);
		this.setLocation(location);
		this.setPhone(phone);
	}

	public Set<Role> getRoles() {
		return roles;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		roles.add(role);
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = getRoles();
		if (roles == null) {
			return Collections.emptyList();
		}
		Set<GrantedAuthority> authorities = roles.stream().map( t-> new SimpleGrantedAuthority(t.getRoleName()
				.toString())).collect(Collectors.toSet());

		return authorities;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Set<Advert> getAdverts() {
		return adverts;
	}

	public void setAdverts(Set<Advert> adverts) {
		this.adverts = adverts;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if (lat != null ? !lat.equals(user.lat) : user.lat != null) return false;
		if (lng != null ? !lng.equals(user.lng) : user.lng != null) return false;
		if (location != null ? !location.equals(user.location) : user.location != null) return false;
		if (name != null ? !name.equals(user.name) : user.name != null) return false;
		if (password != null ? !password.equals(user.password) : user.password != null) return false;
		if (phone != null ? !phone.equals(user.phone) : user.phone != null) return false;
		if (username != null ? !username.equals(user.username) : user.username != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = username != null ? username.hashCode() : 0;
		result = 31 * result + (password != null ? password.hashCode() : 0);
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (location != null ? location.hashCode() : 0);
		result = 31 * result + (phone != null ? phone.hashCode() : 0);
		result = 31 * result + (lat != null ? lat.hashCode() : 0);
		result = 31 * result + (lng != null ? lng.hashCode() : 0);
		return result;
	}
}
