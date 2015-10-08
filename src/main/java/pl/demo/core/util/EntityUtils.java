package pl.demo.core.util;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.util.ReflectionUtils;

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

    public final static Collection<Class> ANNOTATIONS =
            Collections.unmodifiableList(Arrays.asList(OneToMany.class, ManyToOne.class, ManyToMany.class));

    private EntityUtils(){
        Assert.noObject();
    }

    public static void setFieldValue(final Field field, final Object target, final Object value){
        Assert.notNull(field, "Pass field to the method");
        Assert.notNull(target, "Pass target object to the method");
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, target, value);
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

    public static Object initializeHibernateEntity(final Object entity){
        if(entity == null){
            return null;
        }
        Hibernate.initialize(entity);
        if(entity instanceof HibernateProxy){
            return ((HibernateProxy)entity).getHibernateLazyInitializer().getImplementation();
        }
        return entity;
    }
}
