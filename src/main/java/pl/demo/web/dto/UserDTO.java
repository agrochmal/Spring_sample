package pl.demo.web.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;

public class UserDTO {
	
	private String username;
	private String name;
	private String location;
	private String phone;
	private Map<String, Boolean> roles;

	public UserDTO(String username, String name, String location, String phone, Map<String, Boolean> roles) {
		this.setUsername(username);
		this.setName(name);
		this.setLocation(location);
		this.setPhone(phone);
		this.roles = roles;
	}

	public UserDTO(String userName, Map<String, Boolean> roles) {
		this.setName(userName);
		this.roles = roles;
	}

	public String getName() {
		return this.name;
	}

	public Map<String, Boolean> getRoles() {
		return this.roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		UserDTO userDTO = (UserDTO) o;

		return new EqualsBuilder()
				.append(username, userDTO.username)
				.append(name, userDTO.name)
				.append(location, userDTO.location)
				.append(phone, userDTO.phone)
				.append(roles, userDTO.roles)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(username)
				.append(name)
				.append(location)
				.append(phone)
				.append(roles)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("username", username)
				.append("name", name)
				.append("location", location)
				.append("phone", phone)
				.append("roles", roles)
				.toString();
	}
}