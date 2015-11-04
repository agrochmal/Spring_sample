package pl.demo.core.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
/**
 * Created by Robert on 15.02.15.
 */
public class AuthenticationUserDetails implements UserDetails, Serializable {

    private final Long id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> grantedAuthorities;

    public AuthenticationUserDetails(final User user) {
        this.id = user.getId();
        this.username = user.getContact().getEmail();
        this.password = user.getPassword();
        this.grantedAuthorities = user.getAuthorities();
    }

    @JsonProperty(value = "roles")
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return id;
    }
}
