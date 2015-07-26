package pl.demo.web.dto;

/**
 * Created by Robert on 29.12.14.
 */
public final class DashbordDTO {

    private final long users;
    private final long adverts;

    public DashbordDTO(final long users, final long adverts){
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
