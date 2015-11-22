package pl.demo.core.model.entity.preprocessors;

import org.springframework.util.Assert;
import pl.demo.core.model.entity.BaseEntity;
import pl.demo.core.service.security.AuthenticationContextProvider;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

/**
 * Created by robertsikora on 05.11.2015.
 */

public class UpdateUserDateInfoEntityProcessor implements EntityProcessor {

    private final static String SYSTEM_USER = "system";

    @PrePersist
    @PreUpdate
    public void process(final BaseEntity entity){
        Assert.notNull(entity, "The entity to process is mandatory !");

        entity.setEntryDate(new Date());
        if(AuthenticationContextProvider.isAuthenticatedUser()){
            entity.setEntryUser(AuthenticationContextProvider.getAuthenticatedUser().getUsername());
        } else {
            entity.setEntryUser(SYSTEM_USER);
        }
    }
}
