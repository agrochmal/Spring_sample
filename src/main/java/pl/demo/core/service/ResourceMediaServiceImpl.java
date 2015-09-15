package pl.demo.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.MsgConst;
import pl.demo.core.model.entity.MediaResource;
import pl.demo.core.model.repo.MediaResourceRepository;
import pl.demo.core.service.MediaProviders.MediaProvider;
import pl.demo.core.util.SpringBeanProvider;
import pl.demo.core.util.Utils;
import pl.demo.web.exception.GeneralException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Serializable;


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
    public Long upload(final MultipartFile file) {
        Assert.notNull(file);
        MediaResource mediaResource;
        try {
            mediaResource = save(createMediaResource(file));
            this.mediaProvider.upload(Utils.getBytes(file),
                t->  {
                    final ResourceMediaService resourceMediaService = (ResourceMediaService)SpringBeanProvider.getBean("resourceMediaService");
                    resourceMediaService.saveOnCallback(mediaResource.getId(), (String) t.getPublicID());
                });
        } catch (final IOException e) {
            LOGGER.error("", e);
            throw new GeneralException(MsgConst.MEDIA_PROVIDER_ISSUE, e);
        }
        return mediaResource.getId();
    }

    @Transactional
    public void saveOnCallback(final Long id, final String publicId){
        final MediaResource dbMediaResource = getMediaResourceRepository().findOneForUpdate(id);
        dbMediaResource.setPublicId(publicId);
        save(dbMediaResource);
        if(dbMediaResource.getDelete()){
            deleteImage(dbMediaResource.getId());
        }
    }

    private MediaResource createMediaResource(final MultipartFile file){
        return  MediaResource.MediaResourceBuilder.aMediaResource().
                withName(file.getName()).withContentType(file.getContentType()).withSize(file.getSize()).build();
    }

    @Override
    public void deleteImage(final Serializable id) {
        Assert.notNull(id);
        try {
            final Long mediaId = (Long)id;
            final MediaResource mediaResource = getMediaResourceRepository().findOneForUpdate(mediaId);
            if(null != mediaResource.getPublicId()) {
                this.mediaProvider.delete(mediaResource.getPublicId());
                super.delete(mediaId);
            }else{ //storing in cloud is not finished yet. Resource is only mark now for later deletion
                mediaResource.setDelete(Boolean.TRUE);
            }
        } catch (final IOException e) {
            LOGGER.error("", e);
            throw new GeneralException(MsgConst.MEDIA_PROVIDER_ISSUE, e);
        }
    }

    private MediaResourceRepository getMediaResourceRepository(){
        return (MediaResourceRepository) getDomainRepository();
    }

    @Autowired
    public void setMediaProvider(final MediaProvider mediaProvider) {
        this.mediaProvider = mediaProvider;
    }
}
