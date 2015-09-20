package pl.demo.web;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import pl.demo.core.model.entity.MediaResource;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by robertsikora on 17.09.15.
 */

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class HttpSessionContext {

    private Set<MediaResource> uploadedResources = new HashSet<>();

    public void addResource(final MediaResource mediaResource){
        this.uploadedResources.add(mediaResource);
    }
}
