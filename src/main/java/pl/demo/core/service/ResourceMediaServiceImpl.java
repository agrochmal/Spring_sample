package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.demo.core.service.MediaProviders.MediaProvider;

import java.io.IOException;

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
    public void upload(final byte[] file) {
        Assert.notNull(file);
        try {
            this.mediaProvider.upload(file);
        } catch (IOException e) {
            e.printStackTrace(); // refactor !!!
        }
    }
}
