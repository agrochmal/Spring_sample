package pl.demo.core.model.entity.preprocessors;

import pl.demo.core.model.entity.BaseEntity;
import pl.demo.core.service.security.AuthenticationContextProvider;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

/**
 * Created by robertsikora on 05.11.2015.
 */

public class EnrichBaseEntityProcessor {

    private final static String SYSTEM_USER = "system";

    @PrePersist
    @PreUpdate
    public void updateEntryUser(final BaseEntity entity){

        entity.setEntryDate(new Date());
        if(AuthenticationContextProvider.isAuthenticatedUser()){
            entity.setEntryUser(AuthenticationContextProvider.getAuthenticatedUser().getUsername());
        } else {
            entity.setEntryUser(SYSTEM_USER);
        }
    }

}
