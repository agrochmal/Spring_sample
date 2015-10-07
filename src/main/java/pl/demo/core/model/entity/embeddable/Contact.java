package pl.demo.core.model.entity.embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;

import static pl.demo.core.model.entity.ModelConstans.*;

/**
 * Created by robertsikora on 06.10.15.
 */

@Embeddable
public class Contact {

    @NotNull
    @Length(max=TEXT_LENGTH_100)
    @Email
    @Basic
    @Column(length=TEXT_LENGTH_100, nullable = false)
    private String email;

    @NotNull
    @Length(min=TEXT_LENGTH_1, max=TEXT_LENGTH_80)
    @Basic
    @Column(length=TEXT_LENGTH_25, nullable = false)
    private String phone;

    @Embedded
    private Location location;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        return new EqualsBuilder()
                .append(email, contact.email)
                .append(phone, contact.phone)
                .append(location, contact.location)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(email)
                .append(phone)
                .append(location)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("email", email)
                .append("phone", phone)
                .append("location", location)
                .toString();
    }
}
