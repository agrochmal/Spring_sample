package pl.demo.core.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by robertsikora on 29.07.15.
 */

@Transactional(readOnly = true)
@Validated
public interface ResourceMediaService {
    @NotNull
    Long upload(@NotNull MultipartFile file);

    void deleteImage(@NotNull Serializable id);

    void saveOnCallback(@NotNull Long id, @NotNull String publicId);
}
