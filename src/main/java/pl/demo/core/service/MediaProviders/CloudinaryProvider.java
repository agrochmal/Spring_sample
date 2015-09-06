package pl.demo.core.service.MediaProviders;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;

/**
 * Created by robertsikora on 29.07.15.
 */

@Service
public class CloudinaryProvider implements MediaProvider{

    @Autowired
    private Cloudinary cloudinary;

    public CloudinaryUploadResult upload(final Object file) throws IOException {
        Assert.notNull(file);
        final Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        //TO-DO convert map to object
        return null;
    }

    public void setCloudinary(final Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
}
