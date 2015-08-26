package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.demo.core.model.entity.MediaResource;
import pl.demo.core.service.MediaProviders.MediaProvider;

import java.util.Collection;

/**
 * Created by robertsikora on 29.07.15.
 */

@Service
public class ResourceMediaServiceImpl implements ResourceMediaService{

    private final MediaProvider mediaProvider;

    @Autowired
    public ResourceMediaServiceImpl(final MediaProvider mediaProvider){
        this.mediaProvider = mediaProvider;
    }

    @Override
    public void uploadImage(final byte[] image) {
        System.out.print("TO-DO");
    }

    @Override
    public String getImageUrl(MediaResource mediaResource) {
        return null;
    }

    @Override
    public Collection<MediaResource> getAll(Long advertId) {
        return null;
    }
}
