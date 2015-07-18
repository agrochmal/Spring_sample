package pl.demo.core.service;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.ui.velocity.VelocityEngineFactory;

import java.util.Properties;

/**
 * Created by Robert on 21.02.15.
 */
public class VelocityFactory extends AbstractFactoryBean<VelocityEngine> {

    @Override
    public Class<?> getObjectType() {
        return VelocityEngine.class;
    }

    @Override
    protected VelocityEngine createInstance() throws Exception {
        VelocityEngineFactory factory = new VelocityEngineFactory();
        factory.setOverrideLogging(false);
        Properties props = new Properties();
        props.put("resource.loader", "class");
        props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        factory.setVelocityProperties(props);
        return factory.createVelocityEngine();
    }
}
