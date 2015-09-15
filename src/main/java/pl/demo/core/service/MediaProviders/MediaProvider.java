package pl.demo.core.service.MediaProviders;

import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Created by robertsikora on 29.07.15.
 */
@Validated
public interface MediaProvider {
    @Async(value="resourceMediaExecutor")
    UploadResult upload(@NotNull Object file, Consumer<UploadResult> asyncCallback) throws IOException;

    void delete(@NotNull Serializable id) throws IOException;
}
