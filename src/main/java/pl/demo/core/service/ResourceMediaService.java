package pl.demo.core.service;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import pl.demo.core.service.MediaProviders.UploadResult;

/**
 * Created by robertsikora on 29.07.15.
 */
@Validated
public interface ResourceMediaService {
    UploadResult upload(@NotEmpty byte[] image);
}
