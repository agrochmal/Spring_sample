package pl.demo.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created by robertsikora on 14.08.15.
 */

@Component
public class SpringBeanProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    public static Object getBean(final String beanName) {
        Assert.hasText(beanName);
        return applicationContext.getBean(normalizeBeanName(beanName));
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        setAppCtx(applicationContext);
    }

    public static void setAppCtx(final ApplicationContext applicationContext){
        SpringBeanProvider.applicationContext = applicationContext;
    }

    private static String normalizeBeanName(final String input){
        return String.valueOf(input.charAt(0)).toLowerCase().concat(input.substring(1, input.length()));
    }
}
