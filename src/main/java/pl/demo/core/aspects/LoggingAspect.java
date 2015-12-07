package pl.demo.core.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by robertsikora on 08.10.15.
 */

@Aspect
public class LoggingAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut(value="execution(* *(..))")
    public void anyMethod() {
    }

    @Around("anyMethod()")
    public Object aspectAction(final ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }
}
