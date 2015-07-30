package pl.demo.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by robertsikora on 26.07.15.
 */
public final class EntityUtils {

    private final static Logger logger = LoggerFactory.getLogger(EntityUtils.class);

    public final static Collection<Class> ANNOTATIONS =
            Collections.unmodifiableList(Arrays.asList(OneToMany.class, ManyToOne.class, ManyToMany.class));

    private EntityUtils(){
        throw new AssertionError("Cannot create object");
    }

    public static void setFieldValue(final Field field, final Object target, final Object value){
        Assert.notNull(field, "Pass field to the method");
        Assert.notNull(target, "Pass target object to the method");
        field.setAccessible(true);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            logger.error("Cannot set value for field:"+field.getName(), e);
        }
    }

    public static void setFieldValues(final Object target, final Object value,
                                      final Collection<Class> fieldAnnotations){
        Assert.notNull(target, "Pass target object to the method");
        Assert.notNull(fieldAnnotations, "Pass annotations to the method");

        Arrays.stream(target.getClass().getDeclaredFields()).forEach(
                t -> Arrays.stream(t.getAnnotations()).forEach(a -> {
                    if (fieldAnnotations.contains(a.annotationType())) {
                        setFieldValue(t, target, value);
                    }
                })
        );
    }
}
