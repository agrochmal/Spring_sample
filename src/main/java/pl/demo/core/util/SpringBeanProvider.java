package pl.demo.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by robertsikora on 14.08.15.
 */

@Component
public class SpringBeanProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    public static Object getBean(final String beanName) {
        return applicationContext.getBean(beanName);
    }
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        SpringBeanProvider.applicationContext = applicationContext;
    }
}
