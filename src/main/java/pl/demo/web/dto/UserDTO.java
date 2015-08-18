package pl.demo.web.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;

public class UserDTO {

	private Long id;
	private String username;
	private String name;
	private String location;
	private String phone;
	private Map<String, Boolean> roles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
				.append(getRoles(), userDTO.getRoles())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(username)
				.append(name)
				.append(location)
				.append(phone)
				.append(getRoles())
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("username", username)
				.append("name", name)
				.append("location", location)
				.append("phone", phone)
				.append("roles", getRoles())
				.toString();
	}

	public void setRoles(Map<String, Boolean> roles) {
		this.roles = roles;
	}


	public static class UserDTOBuilder {
		private Long id;
		private String username;
		private String name;
		private String location;
		private String phone;
		private Map<String, Boolean> roles;

		private UserDTOBuilder() {
		}

		public static UserDTOBuilder anUserDTO() {
			return new UserDTOBuilder();
		}

		public UserDTOBuilder withId(Long id) {
			this.id = id;
			return this;
		}

		public UserDTOBuilder withUsername(String username) {
			this.username = username;
			return this;
		}

		public UserDTOBuilder withName(String name) {
			this.name = name;
			return this;
		}

		public UserDTOBuilder withLocation(String location) {
			this.location = location;
			return this;
		}

		public UserDTOBuilder withPhone(String phone) {
			this.phone = phone;
			return this;
		}

		public UserDTOBuilder withRoles(Map<String, Boolean> roles) {
			this.roles = roles;
			return this;
		}

		public UserDTOBuilder but() {
			return anUserDTO().withId(id).withUsername(username).withName(name).withLocation(location).withPhone(phone).withRoles(roles);
		}

		public UserDTO build() {
			UserDTO userDTO = new UserDTO();
			userDTO.setId(id);
			userDTO.setUsername(username);
			userDTO.setName(name);
			userDTO.setLocation(location);
			userDTO.setPhone(phone);
			userDTO.setRoles(roles);
			return userDTO;
		}
	}
}