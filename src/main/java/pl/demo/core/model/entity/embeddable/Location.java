package pl.demo.core.model.entity.embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_1;
import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_80;

/**
 * Created by robertsikora on 06.10.15.
 */

@Embeddable
public class Location {

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

        Location location = (Location) o;

        return new EqualsBuilder()
                .append(getLocation(), location.getLocation())
                .append(getLat(), location.getLat())
                .append(getLng(), location.getLng())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getLocation())
                .append(getLat())
                .append(getLng())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("location", getLocation())
                .append("lat", getLat())
                .append("lng", getLng())
                .toString();
    }
}
