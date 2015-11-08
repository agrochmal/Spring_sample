package pl.demo.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.demo.MsgConst;
import pl.demo.web.exception.GeneralException;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by robertsikora on 08.11.2015.
 */
public class WebUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(WebUtils.class);

    protected final static String X_FORWARDED_FOR_HEADER = "X-FORWARDED-FOR";

    private WebUtils(){
        Assert.noObject();
    }

    public static HttpServletRequest geHttpServletRequest(){
         final HttpServletRequest httpServletRequest
                 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
         Assert.notNull(httpServletRequest, "HttpServletRequest should exists here !");

        return httpServletRequest;
    }

    public static URI createURI(final Serializable id){
        Assert.notNull(id);
        URI uri;
        try {
            uri = new URI(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/{id}").buildAndExpand(id).toString());
        } catch (final URISyntaxException e) {
            LOGGER.error("Cannot create URI for given id:" + id, e);
            throw new GeneralException(MsgConst.FATAL_ERROR, e);
        }
        return uri;
    }

    public static String getIpAdress(final HttpServletRequest httpServletRequest){
        Assert.notNull(httpServletRequest);
        String ipAddress = httpServletRequest.getHeader(X_FORWARDED_FOR_HEADER);
        if (ipAddress == null) {
            ipAddress = httpServletRequest.getRemoteAddr();
        }
        return ipAddress;
    }
}
