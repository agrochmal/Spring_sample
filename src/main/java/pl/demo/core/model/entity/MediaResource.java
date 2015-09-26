package pl.demo.core.model.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by robertsikora on 29.07.15.
 */

@Entity
@Table(name = "media_resource")
public class MediaResource extends BaseEntity{

    private String publicId;
    private String name;
    private String contentType;
    private Long size;
    private Boolean delete = Boolean.FALSE;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="advert_id")
    private Advert advert;

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    @NotNull
    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Column(nullable = false)
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @NotNull
    @Column(nullable = false)
    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @NotNull
    @Column(nullable = false)
    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Advert getAdvert() {
        return advert;
    }

    public void setAdvert(Advert advert) {
        this.advert = advert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MediaResource that = (MediaResource) o;

        return new EqualsBuilder()
                .append(publicId, that.publicId)
                .append(name, that.name)
                .append(contentType, that.contentType)
                .append(size, that.size)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(publicId)
                .append(name)
                .append(contentType)
                .append(size)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("publicId", publicId)
                .append("name", name)
                .append("contentType", contentType)
                .append("size", size)
                .toString();
    }

    public static class MediaResourceBuilder {
        private String publicId;
        private String name;
        private String contentType;
        private Long size;

        private MediaResourceBuilder() {
        }

        public static MediaResourceBuilder aMediaResource() {
            return new MediaResourceBuilder();
        }

        public MediaResourceBuilder withPublicId(String publicId) {
            this.publicId = publicId;
            return this;
        }

        public MediaResourceBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public MediaResourceBuilder withContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public MediaResourceBuilder withSize(Long size) {
            this.size = size;
            return this;
        }

        public MediaResourceBuilder but() {
            return aMediaResource().withPublicId(publicId).withName(name).withContentType(contentType).withSize(size);
        }

        public MediaResource build() {
            MediaResource mediaResource = new MediaResource();
            mediaResource.setPublicId(publicId);
            mediaResource.setName(name);
            mediaResource.setContentType(contentType);
            mediaResource.setSize(size);
            return mediaResource;
        }
    }
}
