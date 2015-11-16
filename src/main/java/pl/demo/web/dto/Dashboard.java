package pl.demo.web.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by Robert on 29.12.14.
 */
public final class Dashboard {

    private final long users;
    private final long adverts;
    private final long comments;

    private Dashboard(final long users, final long adverts, final long comments){
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

    public long getComments() {
        return comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Dashboard that = (Dashboard) o;

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


    public static class DashboardDTOBuilder {
        private long users;
        private long adverts;
        private long comments;

        private DashboardDTOBuilder() {
        }

        public static DashboardDTOBuilder aDashboardDTO() {
            return new DashboardDTOBuilder();
        }

        public DashboardDTOBuilder withUsers(long users) {
            this.users = users;
            return this;
        }

        public DashboardDTOBuilder withAdverts(long adverts) {
            this.adverts = adverts;
            return this;
        }

        public DashboardDTOBuilder withComments(long comments) {
            this.comments = comments;
            return this;
        }

        public DashboardDTOBuilder but() {
            return aDashboardDTO().withUsers(users).withAdverts(adverts).withComments(comments);
        }

        public Dashboard build() {
            Dashboard dashboardDTO = new Dashboard(users, adverts, comments);
            return dashboardDTO;
        }
    }
}
