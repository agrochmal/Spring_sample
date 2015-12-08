package pl.demo.web.lookup.paging;

import java.util.List;

/**
 * Created by robertsikora on 08.12.2015.
 */
public interface Page<T>{

    List<T> getContent();

    int getTotal();
}
