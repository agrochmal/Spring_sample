package pl.demo.web.lookup;

import pl.demo.web.lookup.paging.Pageable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * Created by robertsikora on 08.12.2015.
 */
public class SearchCriteria implements Pageable {

    @Min(0)
    private int page;
    @Min(1)
    private int size;
    @NotNull
    private Map<String, String> criteria;

    @Override
    public int getPageNumber() {
        return page;
    }

    @Override
    public int getPageSize() {
        return size;
    }

    public void setPage(final int page) {
        this.page = page;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public Map<String, String> getCriteria() {
        return criteria;
    }

    public void setCriteria(final Map<String, String> criteria) {
        this.criteria = criteria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchCriteria that = (SearchCriteria) o;
        return page == that.page &&
                size == that.size &&
                Objects.equals(criteria, that.criteria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, size, criteria);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchCriteria{");
        sb.append("page=").append(page);
        sb.append(", size=").append(size);
        sb.append(", criteria=").append(criteria);
        sb.append('}');
        return sb.toString();
    }
}
