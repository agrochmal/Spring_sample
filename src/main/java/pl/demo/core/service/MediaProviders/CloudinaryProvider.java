package pl.demo.core.service.MediaProviders;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.demo.MsgConst;
import pl.demo.web.exception.GeneralException;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by robertsikora on 29.07.15.
 */

@Service
public class CloudinaryProvider implements MediaProvider{

    private Cloudinary cloudinary;

    @Override
    public CloudinaryUploadResult upload(final Object file, Consumer<UploadResult> asyncCallback) throws IOException {
        Assert.notNull(file);
        final Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        final CloudinaryUploadResult cloudinaryUploadResult = new CloudinaryUploadResult(uploadResult);
        if(null != asyncCallback) {
            asyncCallback.accept(cloudinaryUploadResult);
        }
        return cloudinaryUploadResult;
    }

    @Override
    public void delete(final Serializable id) throws IOException  {
        Assert.notNull(id);
        final Map deleteResult = cloudinary.uploader().destroy((String)id, ObjectUtils.emptyMap());
        if(!deleteResult.getOrDefault("result", "").equals("ok")){
            throw new GeneralException(MsgConst.CANNOT_DELETE_IMAGE);
        }
    }

    @Autowired
    public void setCloudinary(final Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
}
