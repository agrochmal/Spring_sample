package pl.demo.core.model.entity.versioning;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.demo.core.model.entity.BaseEntity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Created by robertsikora on 07.11.2015.
 */

@MappedSuperclass
public class VersionableBaseEntity extends BaseEntity {

    @Version
    private int version;

    @JsonIgnore
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
