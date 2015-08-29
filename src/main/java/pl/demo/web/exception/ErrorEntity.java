package pl.demo.web.exception;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by robertsikora on 01.08.15.
 */
public final class ErrorEntity {

    private final String errorMsg;
    private final String developerMsg;

    public ErrorEntity(final String errorMsg, final String developerMsg) {
        this.errorMsg = errorMsg;
        this.developerMsg = developerMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getDeveloperMsg() {
        return developerMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ErrorEntity that = (ErrorEntity) o;

        return new EqualsBuilder()
                .append(errorMsg, that.errorMsg)
                .append(developerMsg, that.developerMsg)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(errorMsg)
                .append(developerMsg)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("errorMsg", errorMsg)
                .append("developerMsg", developerMsg)
                .toString();
    }
}
