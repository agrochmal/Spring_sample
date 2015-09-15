package pl.demo.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import pl.demo.MsgConst;
import pl.demo.core.model.entity.MediaResource;
import pl.demo.core.service.MediaProviders.MediaProvider;
import pl.demo.web.exception.GeneralException;

import javax.transaction.Transactional;
import java.io.IOException;


/**
 * Created by robertsikora on 29.07.15.
 */

@Transactional
public class ResourceMediaServiceImpl extends CRUDServiceImpl<Long, MediaResource>
        implements ResourceMediaService{

    private final static Logger LOGGER = LoggerFactory.getLogger(ResourceMediaServiceImpl.class);

    private MediaProvider mediaProvider;

    @Override
    protected Class<MediaResource> supportedDomainClass() {
        return MediaResource.class;
    }

    @Override
    public Long upload(final byte[] file) {
        Assert.notNull(file);
        MediaResource mediaResource;
        try {
            mediaResource = getDomainRepository().saveAndFlush(new MediaResource());
            this.mediaProvider.upload(file, t->{
                final MediaResource dbMediaResource = findOne(mediaResource.getId());
                dbMediaResource.setPublicId((String)t.getPublicID());
                save(dbMediaResource);
            });
        } catch (final IOException e) {
            LOGGER.error("", e);
            throw new GeneralException(MsgConst.MEDIA_PROVIDER_ISSUE, e);
        }
        return mediaResource.getId();
    }


    @Override
    public void delete(final Long id) {
        Assert.notNull(id);
        try {
            getDomainRepository().delete(id);
            this.mediaProvider.delete(id);
        } catch (final IOException e) {
            LOGGER.error("", e);
            throw new GeneralException(MsgConst.MEDIA_PROVIDER_ISSUE, e);
        }
    }

    @Autowired
    public void setMediaProvider(final MediaProvider mediaProvider) {
        this.mediaProvider = mediaProvider;
    }
}
