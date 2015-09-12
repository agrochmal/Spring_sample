package pl.demo.core.service;

import org.springframework.validation.annotation.Validated;
import pl.demo.core.service.MediaProviders.UploadResult;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by robertsikora on 29.07.15.
 */
@Validated
public interface ResourceMediaService {
    UploadResult upload(@NotNull byte[] image);
    void delete(@NotNull Serializable id);
}
