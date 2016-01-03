package pl.demo.core.service.mediaproviders;

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

    public CloudinaryUploadResult(final Map<String, String> map){
        Assert.notNull(map);
        this.resultMap = map;
    }

    public String getSignature(){
        return resultMap.get(SIGNATURE);
    }

    public String getFormat(){
        return resultMap.get(FORMAT);
    }

    public String getResourceType(){
        return resultMap.get(RESOURCE_TYPE);
    }

    public String getCreatedAt(){
        return resultMap.get(CREATED_AT);
    }

    public String getSecureUrl(){
        return resultMap.get(SECURE_URL);
    }

    public String getEtag(){
        return resultMap.get(ETAG);
    }

    public String getTags(){
        return resultMap.get(TAGS);
    }

    public String getWidth(){
        return resultMap.get(WIDTH);
    }

    public String getHeight(){
        return resultMap.get(HEIGHT);
    }

    public String getType(){
        return resultMap.get(TYPE);
    }

    public String getVersion(){
        return resultMap.get(VERSION);
    }

    public String getUrl(){
        return resultMap.get(URL);
    }

    public String getOrginalFilename(){
        return resultMap.get(ORGINAL_FILENAME);
    }

    public String getBytes(){
        return resultMap.get(BYTES);
    }

    @Override
    public String getPublicID(){
        return resultMap.get(PUBLIC_ID);
    }

    private String getValue(final String key){
        Assert.hasText(key);
        return resultMap.get(key);
    }
}
