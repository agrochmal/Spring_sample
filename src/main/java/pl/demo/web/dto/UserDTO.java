package pl.demo.web.dto;

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
}