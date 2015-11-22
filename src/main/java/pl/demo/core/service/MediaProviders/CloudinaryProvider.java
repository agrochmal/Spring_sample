package pl.demo.core.service.MediaProviders;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.demo.MsgConst;
import pl.demo.web.exception.ServerException;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by robertsikora on 29.07.15.
 */

@Service
public class CloudinaryProvider implements MediaProvider{

    private final static String RESULT_VAR = "result";
    private final static String RESULT_OK = "ok";

    private final static String THUMB_FORMAT = "jpg";
    private final static int THUMB_H = 250;
    private final static int THUMB_W = 250;
    private final static String THUMB_CROP = "fit";

    private Cloudinary cloudinary;

    private CloudinaryUploadResult upload(final Object file, Consumer<UploadResult> asyncCallback) {
        Assert.notNull(file);
        Map uploadResult;
        try {
            uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        }catch(final RuntimeException | IOException e){
            throw new ServerException(e.getMessage());
        }
        final CloudinaryUploadResult cloudinaryUploadResult = new CloudinaryUploadResult(uploadResult);
        if(null != asyncCallback) {
            asyncCallback.accept(cloudinaryUploadResult);
        }
        return cloudinaryUploadResult;
    }

    @Override
    public UploadResult uploadSync(final Object file, final Consumer<UploadResult> asyncCallback) throws IOException {
        return upload(file, asyncCallback);
    }

    @Override
    public UploadResult uploadAsync(final Object file, final Consumer<UploadResult> asyncCallback) throws IOException {
        return upload(file, asyncCallback);
    }

    @Override
    public void delete(final Serializable id) throws IOException  {
        Assert.notNull(id);
        final Map deleteResult = cloudinary.uploader().destroy((String)id, ObjectUtils.emptyMap());
        if(!deleteResult.getOrDefault(RESULT_VAR, StringUtils.EMPTY).equals(RESULT_OK)){
            throw new ServerException(MsgConst.CANNOT_DELETE_IMAGE);
        }
    }

    @Override
    public String getThumb(final Serializable id) {
        return cloudinary.url().format(THUMB_FORMAT)
                .transformation(new Transformation().width(THUMB_W).height(THUMB_H).crop(THUMB_CROP))
                .generate((String)id);
    }

    @Autowired
    public void setCloudinary(final Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
}
