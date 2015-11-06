package pl.demo.core.service.resource;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.core.model.entity.MediaResource;
import pl.demo.core.service.basic_service.CRUDService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by robertsikora on 29.07.15.
 */

@Validated
public interface ResourceMediaService extends CRUDService<Long, MediaResource> {

    @PreAuthorize("isAuthenticated()")
    @NotNull
    @Min(1)
    Long upload(@NotNull MultipartFile file);

    @PreAuthorize("isAuthenticated()")
    void deleteImage(@NotNull Serializable id);

    @PreAuthorize("isAuthenticated()")
    void saveOnCallback(@NotNull @Min(1) long id, @NotNull @NotBlank String publicId);

    @PreAuthorize("isAuthenticated()")
    @NotNull
    @NotBlank
    String getThumb(@NotNull Serializable id);
}
