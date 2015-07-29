package pl.demo.web.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class TokenDTO {

	private final String token;

	public TokenDTO(final String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		TokenDTO tokenDTO = (TokenDTO) o;

		return new EqualsBuilder()
				.append(token, tokenDTO.token)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(token)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("token", token)
				.toString();
	}
}