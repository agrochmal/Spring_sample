package pl.demo.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.demo.MsgConst;
import pl.demo.core.service.MediaProviders.MediaProvider;
import pl.demo.core.service.MediaProviders.UploadResult;
import pl.demo.web.exception.GeneralException;

import java.io.IOException;
import java.io.Serializable;

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
            throw new GeneralException(MsgConst.MEDIA_PROVIDER_ISSUE, e);
        }
    }

    @Override
    public void delete(final Serializable id) {
        Assert.notNull(id);
        try {
            this.mediaProvider.delete(id);
        } catch (IOException e) {
            LOGGER.error("", e);
            throw new GeneralException(MsgConst.MEDIA_PROVIDER_ISSUE, e);
        }
    }
}
