package pl.demo.core.service;

import pl.demo.core.service.MediaProviders.UploadResult;

/**
 * Created by robertsikora on 29.07.15.
 */
public interface ResourceMediaService {
    UploadResult upload(byte[] image);
}
