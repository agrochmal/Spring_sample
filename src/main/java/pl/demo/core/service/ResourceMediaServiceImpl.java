package pl.demo.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.demo.core.service.MediaProviders.MediaProvider;
import pl.demo.core.service.MediaProviders.UploadResult;
import pl.demo.web.exception.GeneralException;

import java.io.IOException;

/**
 * Created by robertsikora on 29.07.15.
 */

@Service
public class ResourceMediaServiceImpl implements ResourceMediaService{

    private final static Logger LOGGER = LoggerFactory.getLogger(ResourceMediaServiceImpl.class);

    private final MediaProvider mediaProvider;

    @Autowired
    public ResourceMediaServiceImpl(final MediaProvider mediaProvider){
        this.mediaProvider = mediaProvider;
    }

    @Override
    public UploadResult upload(final byte[] file) {
        Assert.notNull(file);
        try {
            return this.mediaProvider.upload(file);
        } catch (IOException e) {
            LOGGER.error("", e);
            throw new GeneralException(e.getMessage(), e);
        }
    }
}
