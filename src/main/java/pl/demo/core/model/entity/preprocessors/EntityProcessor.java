package pl.demo.core.model.entity.preprocessors;

import pl.demo.core.model.entity.BaseEntity;

/**
 * Created by robertsikora on 16.11.2015.
 */
public interface EntityProcessor {

    void process(BaseEntity baseEntity);
}
