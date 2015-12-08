package pl.demo.web.lookup;

import pl.demo.core.model.dictionary.Category;

import java.util.Collections;
import java.util.List;

/**
 * Created by robertsikora on 04.12.2015.
 */
public class CategoryLookup implements Lookup<Category> {

    @Override
    public List<Category> getData() {
        Category category = new Category();
        category.setName("Kategoria 1");
        return Collections.singletonList(category);
    }
}
