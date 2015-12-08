package pl.demo.web.lookup;

import java.util.List;

/**
 * Created by robertsikora on 04.12.2015.
 */
public interface Lookup <T> {

    List<T> getData();
}
