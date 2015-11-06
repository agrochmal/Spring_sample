package pl.demo.core.model.entity.preprocessors;

import pl.demo.core.model.entity.BaseEntity;
import pl.demo.core.service.authentication.AuthenticationService;
import pl.demo.core.util.SpringBeanProvider;

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

        final AuthenticationService authenticationService =
                (AuthenticationService)SpringBeanProvider.getBean("authenticationService");

        entity.setEntryDate(new Date());

        if(authenticationService.isAuthenticatedUser()){
            entity.setEntryUser(authenticationService.getAuthenticatedUser().getUsername());
        } else {
            entity.setEntryUser(SYSTEM_USER);
        }
    }

}
