package pl.demo.core.model.entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_1;
import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_80;

/**
 * Created by robertsikora on 06.10.15.
 */

@Embeddable
public class Contact {

    @NotNull
    @Length(min=TEXT_LENGTH_1, max=TEXT_LENGTH_80)
    @Email
    @Basic
    @Column(length=TEXT_LENGTH_80, nullable = false)
    private String email;

    @NotNull
    @Length(min=TEXT_LENGTH_1, max=TEXT_LENGTH_80)
    @Basic
    @Column(length=TEXT_LENGTH_80, nullable = false)
    private String phone;

    @NotNull
    @Length(min=TEXT_LENGTH_1, max=TEXT_LENGTH_80)
    @Basic
    @Column(length=TEXT_LENGTH_80, nullable=false)
    private String location;

    @NotNull
    @Basic
    @Column(nullable=false)
    private Double lat = 0d;

    @NotNull
    @Basic
    @Column(nullable = false)
    private Double lng = 0d;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(email, contact.email) &&
                Objects.equals(phone, contact.phone) &&
                Objects.equals(location, contact.location) &&
                Objects.equals(lat, contact.lat) &&
                Objects.equals(lng, contact.lng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, phone, location, lat, lng);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Contact{");
        sb.append("email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", lat=").append(lat);
        sb.append(", lng=").append(lng);
        sb.append('}');
        return sb.toString();
    }
}
