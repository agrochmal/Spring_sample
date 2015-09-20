package pl.demo.core.aspects;

import java.lang.annotation.*;

/**
 * Created by robertsikora on 17.09.15.
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DetachEntity {


}
