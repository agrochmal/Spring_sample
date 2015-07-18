package pl.demo.web.dto;

/**
 * Created by Robert on 29.12.14.
 */
public class DashbordDTO {

    private Long users;
    private Long adverts;

    public DashbordDTO(Long users, Long adverts){
        this.users=users;
        this.adverts=adverts;
    }

    public Long getUsers() {
        return users;
    }

    public void setUsers(Long users) {
        this.users = users;
    }

    public Long getAdverts() {
        return adverts;
    }

    public void setAdverts(Long adverts) {
        this.adverts = adverts;
    }
}
