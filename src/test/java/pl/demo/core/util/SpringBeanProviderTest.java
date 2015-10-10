package pl.demo.core.util;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import pl.demo.ApplicationContextFake;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by robertsikora on 15.09.15.
 */

@RunWith(MockitoJUnitRunner.class)
public class SpringBeanProviderTest {

    private final static String SAMPLE_BEAN_NAME = "sampleBean";
    private final static Integer SAMPLE_BEAN = Integer.valueOf(1);

    @Mock
    private ApplicationContext applicationContext;

    @Test(expected = IllegalArgumentException.class)
    public void testGetBeanNullArg() throws Exception {
        SpringBeanProvider.getBean(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBeanEmptyArg() throws Exception {
        SpringBeanProvider.getBean(StringUtils.EMPTY);
    }

    @Test
    public void testGetBean() throws Exception {
        when(applicationContext.getBean(SAMPLE_BEAN_NAME)).thenReturn(SAMPLE_BEAN);
        SpringBeanProvider.setAppCtx(applicationContext);
        assertEquals(SAMPLE_BEAN, SpringBeanProvider.getBean(SAMPLE_BEAN_NAME));
    }

    @Test
    public void testGetBeanWithNormalization() throws Exception {
        when(applicationContext.getBean(SAMPLE_BEAN_NAME)).thenReturn(SAMPLE_BEAN);
        SpringBeanProvider.setAppCtx(applicationContext);
        assertEquals(SAMPLE_BEAN, SpringBeanProvider.getBean("SampleBean"));
    }

    @Test
    public void testSetApplicationContext() throws Exception {
        final SpringBeanProvider springBeanProvider = new SpringBeanProvider();
        final ApplicationContext appCtx = ApplicationContextFake.getApplicationContext(String.class);
        springBeanProvider.setApplicationContext(appCtx);
        assertEquals(appCtx, springBeanProvider.applicationContext);
    }
}