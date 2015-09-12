package pl.demo.core.service.MediaProviders;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by robertsikora on 29.07.15.
 */
@Validated
public interface MediaProvider {
    UploadResult upload(@NotNull Object file) throws IOException;
    void delete(@NotNull Serializable id) throws IOException;
}
