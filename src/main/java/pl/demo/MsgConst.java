package pl.demo;

import pl.demo.core.util.Assert;

/**
 * Created by robertsikora on 06.09.15.
 */
public final class MsgConst {

    private MsgConst(){
        Assert.noObject();
    }

    public static final String FATAL_ERROR = "FATAL_ERROR";
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String VALIDATION_FAILED = "VALIDATION_FAILED";
    public static final String FILE_EMPTY = "FILE.IS_EMPTY";
    public static final String FILE_TOO_BIG = "FILE.IS_TOO_BIG";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String CANNOT_DELETE_IMAGE = "CANNOT_DELETE_IMAGE";
    public static final String MEDIA_PROVIDER_ISSUE = "MEDIA_PROVIDER_ISSUE";
}
