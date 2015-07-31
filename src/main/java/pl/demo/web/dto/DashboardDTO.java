package pl.demo.web.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by Robert on 29.12.14.
 */
public final class DashboardDTO {

    private final long users;
    private final long adverts;
    private final long comments;

    public DashboardDTO(final long users, final long adverts, final long comments){
        this.users=users;
        this.adverts=adverts;
        this.comments=comments;
    }

    public long getUsers() {
        return users;
    }

    public long getAdverts() {
        return adverts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DashboardDTO that = (DashboardDTO) o;

        return new EqualsBuilder()
                .append(users, that.users)
                .append(adverts, that.adverts)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(users)
                .append(adverts)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("users", users)
                .append("adverts", adverts)
                .toString();
    }
}
