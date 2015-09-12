package pl.demo.core.service.MediaProviders;

import java.io.IOException;

/**
 * Created by robertsikora on 29.07.15.
 */
public interface MediaProvider {
    UploadResult upload(Object file) throws IOException;
}
