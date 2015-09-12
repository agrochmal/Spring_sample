package pl.demo.core.service.MediaProviders;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by robertsikora on 29.07.15.
 */
@Validated
public interface MediaProvider {
    UploadResult upload(@NotEmpty Object file) throws IOException;
    void delete(@NotEmpty Serializable id) throws IOException;
}
