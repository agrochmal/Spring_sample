package pl.demo.web.dto;

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
}


