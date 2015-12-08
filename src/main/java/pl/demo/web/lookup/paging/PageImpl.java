package pl.demo.web.lookup.paging;

import java.util.List;

/**
 * Created by robertsikora on 08.12.2015.
 */
public class PageImpl<T> implements Page<T>{

    final List<T> content;
    final int total;
    private final Pageable pageable;

    public PageImpl(final List<T> content, final int total, final Pageable pageable) {
        this.content = content;
        this.total = total;
        this.pageable = pageable;
    }

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public int getTotal() {
        return total;
    }

    public Pageable getPageable() {
        return pageable;
    }
}
