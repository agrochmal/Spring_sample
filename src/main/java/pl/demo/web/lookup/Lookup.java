package pl.demo.web.lookup;

import java.util.Collection;

/**
 * Created by robertsikora on 04.12.2015.
 */
public interface Lookup <T> {

    Collection<T> getData();
}
