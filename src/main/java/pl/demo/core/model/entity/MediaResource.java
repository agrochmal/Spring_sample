package pl.demo.core.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by robertsikora on 29.07.15.
 */

@Entity
@Table(name = "media_resource")
public class MediaResource extends BaseEntity{

    private String publicId;

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
}
