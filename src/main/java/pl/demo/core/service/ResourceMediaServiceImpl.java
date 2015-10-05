package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.MsgConst;
import pl.demo.core.model.entity.MediaResource;
import pl.demo.core.model.repo.MediaResourceRepository;
import pl.demo.core.service.MediaProviders.MediaProvider;
import pl.demo.core.util.SpringBeanProvider;
import pl.demo.core.util.Utils;
import pl.demo.web.HttpSessionContext;
import pl.demo.web.exception.GeneralException;

import java.io.IOException;
import java.io.Serializable;


/**
 * Created by robertsikora on 29.07.15.
 */

public class ResourceMediaServiceImpl extends CRUDServiceImpl<Long, MediaResource> implements ResourceMediaService{

    private final static String DEFAULT_IMAGE_THUMB = "./app/images/thumb.jpg";

    private HttpSessionContext httpSessionContext;
    private MediaProvider mediaProvider;

    @Transactional
    @Override
    public Long upload(final MultipartFile file) {
        Assert.notNull(file);
        MediaResource mediaResource;
        try {
            mediaResource = save(createMediaResource(file));
            httpSessionContext.addResource(mediaResource.getId());
            this.mediaProvider.uploadSync(Utils.getBytes(file),
            t-> ((ResourceMediaService)SpringBeanProvider.getBean("resourceMediaService"))
                    .saveOnCallback(mediaResource.getId(), (String) t.getPublicID()));
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

    @Transactional(readOnly = true)
    @Override
    public MediaResource getFirst(final Long advertId) {
        return getMediaResourceRepository().findFirstByAdvertOrderByEntryDateDesc(advertId);
    }

    @Override
    public String getThumb(final Serializable id) {
        final MediaResource mediaResource = getFirst((Long)id);
        if(null != mediaResource) {
            return this.mediaProvider.getThumb(mediaResource.getPublicId());
        }
        return DEFAULT_IMAGE_THUMB;
    }

    private MediaResource createMediaResource(final MultipartFile file){
        return MediaResource.MediaResourceBuilder.aMediaResource().
                withName(file.getName()).withContentType(file.getContentType()).withSize(file.getSize()).build();
    }

    @Transactional
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

    @Autowired
    public void setHttpSessionContext(final HttpSessionContext httpSessionContext) {
        this.httpSessionContext = httpSessionContext;
    }
}
