package pl.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.demo.core.util.Assert;
import pl.demo.web.exception.ServerException;
import pl.demo.web.lookup.Lookup;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by robertsikora on 04.12.2015.
 */
@RestController
@RequestMapping("/dictionary")
public class LookupEndpoint implements InitializingBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(LookupEndpoint.class);

    private Map<String, Lookup> lookupMap;

    @RequestMapping(value = "/{lookup}",
            method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)

    public ResponseEntity<Collection> getData(@PathVariable("lookup") final String lookup,
                                              @RequestParam(value = "page", defaultValue = "1") int page,
                                              @RequestParam(value = "size", defaultValue = "100") int size){

        LOGGER.debug("invoking getData for lookup {} with params: page {} and size {}", lookup, page, size);

        final Lookup lookupImpl = lookupMap.get(lookup);
        if(lookupImpl == null){
            throw new ServerException("Dictionary is not supported by API");
        }

        return ResponseEntity.ok().body(lookupImpl.getData());
    }

    @Resource(name="lookups")
    public void setLookupMap(final Map<String, Lookup> lookupMap){
        this.lookupMap = lookupMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(lookupMap);
    }
}
