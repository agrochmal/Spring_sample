package pl.demo.core.service.security;

import org.springframework.stereotype.Component;

/**
 * Created by robertsikora on 04.11.2015.
 */

@Component
public class Permission {

    private Permission(){}

    public static final String HAS_USER_ROLE = "hasRole('USER')";
    public static final String HAS_ADMIN_ROLE = "hasRole('USER')";
}
