package pl.demo.core.service.mail;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.ui.velocity.VelocityEngineFactory;
import pl.demo.web.exception.GeneralException;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Robert on 21.02.15.
 */
public final class VelocityFactory extends AbstractFactoryBean<VelocityEngine> {

    private final static Logger LOGGER = LoggerFactory.getLogger(VelocityFactory.class);

    @Override
    public Class<?> getObjectType() {
        return VelocityEngine.class;
    }

    @Override
    protected VelocityEngine createInstance() {
        final VelocityEngineFactory factory = new VelocityEngineFactory();
        factory.setOverrideLogging(false);
        final Properties props = new Properties();
        props.put("resource.loader", "class");
        props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        factory.setVelocityProperties(props);
        try {
            return factory.createVelocityEngine();
        } catch (IOException e) {
            LOGGER.error("Error during preparing velocity engine", e);
            throw new GeneralException(e.getMessage(), e);
        }
    }
}
