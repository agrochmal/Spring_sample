package pl.demo.web.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.security.core.token.Token;

public class TokenDTO implements Token {

	private final String key;

	public TokenDTO(final String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public long getKeyCreationTime() {
		return 0;
	}

	@Override
	public String getExtendedInformation() {
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		TokenDTO tokenDTO = (TokenDTO) o;

		return new EqualsBuilder()
				.append(key, tokenDTO.key)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(key)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("token", key)
				.toString();
	}
}