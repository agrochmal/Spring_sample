package pl.demo.core.service;

import pl.demo.core.model.entity.MediaResource;

import java.util.Collection;

/**
 * Created by robertsikora on 29.07.15.
 */
public interface ResourceMediaService {

    void uploadImage(byte[] image);

    String getImageUrl(MediaResource mediaResource);

    Collection<MediaResource> getAll(Long advertId);
}
