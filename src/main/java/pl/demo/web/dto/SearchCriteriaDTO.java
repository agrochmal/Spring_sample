package pl.demo.web.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Objects;

/**
 * Created by Robert on 02.12.14.
 */
public class SearchCriteriaDTO {

    private String keyWords;
    private Double locLatitude;
    private Double locLongitude;
    private Double locRadius;

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public Double getLocLatitude() {
        return locLatitude;
    }

    public void setLocLatitude(Double locLatitude) {
        this.locLatitude = locLatitude;
    }

    public Double getLocLongitude() {
        return locLongitude;
    }

    public void setLocLongitude(Double locLongitude) {
        this.locLongitude = locLongitude;
    }

    public Double getLocRadius() {
        return locRadius;
    }

    public void setLocRadius(Double locRadius) {
        this.locRadius = locRadius;
    }

    public boolean isEmpty(){
        return (Objects.isNull(keyWords) || "".equals(keyWords))
                && Objects.isNull(locLatitude)
                 && Objects.isNull(locLongitude);
    }

    public boolean hasLocation(){
        return !Objects.isNull(locLatitude) && !Objects.isNull(locLongitude);
    }

    public boolean hasKeyword(){
        return !(Objects.isNull(keyWords) || "".equals(keyWords));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SearchCriteriaDTO that = (SearchCriteriaDTO) o;

        return new EqualsBuilder()
                .append(keyWords, that.keyWords)
                .append(locLatitude, that.locLatitude)
                .append(locLongitude, that.locLongitude)
                .append(locRadius, that.locRadius)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(keyWords)
                .append(locLatitude)
                .append(locLongitude)
                .append(locRadius)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("keyWords", keyWords)
                .append("locLatitude", locLatitude)
                .append("locLongitude", locLongitude)
                .append("locRadius", locRadius)
                .toString();
    }
}


