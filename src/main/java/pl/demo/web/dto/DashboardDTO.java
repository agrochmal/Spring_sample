package pl.demo.web.dto;

/**
 * Created by Robert on 29.12.14.
 */
public final class DashboardDTO {

    private final long users;
    private final long adverts;

    public DashboardDTO(final long users, final long adverts){
        this.users=users;
        this.adverts=adverts;
    }

    public long getUsers() {
        return users;
    }

    public long getAdverts() {
        return adverts;
    }
}
