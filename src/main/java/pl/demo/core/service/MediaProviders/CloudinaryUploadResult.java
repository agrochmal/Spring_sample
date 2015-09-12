package pl.demo.core.service.MediaProviders;

import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by robertsikora on 06.09.15.
 */
public class CloudinaryUploadResult implements UploadResult{

    private static final String SIGNATURE = "signature";
    private static final String FORMAT = "format";
    private static final String RESOURCE_TYPE = "resource_type";
    private static final String SECURE_URL = "secure_url";
    private static final String CREATED_AT = "created_at";
    private static final String TYPE = "type";
    private static final String VERSION = "version";
    private static final String URL = "url";
    private static final String PUBLIC_ID = "public_id";
    private static final String TAGS = "tags";
    private static final String ORGINAL_FILENAME = "orginal_filename";
    private static final String BYTES = "bytes";
    private static final String WIDTH = "width";
    private static final String ETAG = "etag";
    private static final String HEIGHT = "height";

    private final Map<String, String> resultMap;

    public CloudinaryUploadResult(final Map map){
        this.resultMap = map;
    }

    private String getValue(final String key){
        Assert.hasText(key);
        return resultMap.get(key);
    }

    public CloudinaryUploadResult build(){
        return this;
    }
}
