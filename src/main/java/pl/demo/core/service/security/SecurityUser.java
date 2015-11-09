package pl.demo.core.service.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by robertsikora on 08.11.2015.
 */
public class SecurityUser extends User {

    private Long id;
    private String salt;

    public SecurityUser(final Long id,
                        final String username,
                        final String password,
                        final Collection<? extends GrantedAuthority> authorities,
                        final String salt) {

        super(username, password, authorities);
        this.id = id;
        this.salt = salt;
    }

    public Long getId() {
        return id;
    }

    @JsonIgnore
    public String getSalt() {
        return salt;
    }
}
