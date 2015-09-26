package pl.demo.web;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by robertsikora on 17.09.15.
 */

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class HttpSessionContext {

    private Set<Long> uploadedResourcesId = Collections.emptySet();

    public void addResource(final Long mediaResourceId){
        if(uploadedResourcesId.size() == 0){
            this.uploadedResourcesId = new HashSet<>();
        }
        this.uploadedResourcesId.add(mediaResourceId);
    }

    public Iterator<Long> getUploadedResourcesId() {
        return uploadedResourcesId.iterator();
    }
}
