package pl.demo.core.util;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by robertsikora on 26.07.15.
 */
public final class EntityUtils {

    public final static Collection<String> PERSISTANCE_ANNOTATION_NAMES =
            Collections.unmodifiableList(Arrays.asList("OneToMany", "ManyToOne", "ManyToMany"));

    private EntityUtils(){
        throw new AssertionError("Cannot create object");
    }

    public static void setFieldValue(final Object target, final String fieldName, final Object value){

    }

    public static void setFieldValues(final Object target, Collection<String> fieldAnnotations){

    }
}
