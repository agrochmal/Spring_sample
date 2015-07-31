package pl.demo.core.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.demo.core.util.EntityUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_80;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements FlatableEntity {

	@NotNull
	@Length(max=TEXT_LENGTH_80)

	@Column(unique=true, length=TEXT_LENGTH_80, nullable=false)
	private String username;

	@Length(max=TEXT_LENGTH_80)

	@Column(length=TEXT_LENGTH_80, nullable=false)
	private String password;

	private String name;

	@NotNull
	@Column(nullable=false)
	private String location;

	@NotNull

	@Column(nullable=false)
	private String phone;

	@Column(nullable=false)
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
	public void flatEntity(){
		EntityUtils.setFieldValues(this, null, EntityUtils.ANNOTATIONS);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		return new EqualsBuilder()
				.append(username, user.username)
				.append(password, user.password)
				.append(name, user.name)
				.append(location, user.location)
				.append(phone, user.phone)
				.append(lat, user.lat)
				.append(lng, user.lng)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(username)
				.append(password)
				.append(name)
				.append(location)
				.append(phone)
				.append(lat)
				.append(lng)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("username", username)
				.append("password", password)
				.append("name", name)
				.append("location", location)
				.append("phone", phone)
				.append("lat", lat)
				.append("lng", lng)
				.toString();
	}
}
