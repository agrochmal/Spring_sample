package pl.demo.core.aspects;

/**
 * Created by robertsikora on 17.09.15.
 */

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import pl.demo.core.model.entity.BaseEntity;
import pl.demo.core.model.repo.GenericRepository;

import java.util.Collections;
import java.util.List;

@Aspect
public class DetachEntityAspect {

    private GenericRepository<BaseEntity> genericRepository;

    @Pointcut(value="execution(public * *(..))")
    public void anyPublicServiceMethod() {
    }

    @Around("anyPublicServiceMethod() && @annotation(detachEntity)")
    public Object aspectAction(final ProceedingJoinPoint pjp, final DetachEntity detachEntity) throws Throwable {
        final Object result = pjp.proceed();
        detachAndUnproxyEntity(result);
        return result;
    }

    private void detachAndUnproxyEntity(final Object target){
        List<Object> entities;
        if(target instanceof Page){
            entities = ((Page)target).getContent();
        }else if(target instanceof List){
            entities = (List<Object>) target;
        }else{
            entities = Collections.singletonList(target);
        }
        for(final Object entity : entities) {
            final BaseEntity baseEntity = (BaseEntity)entity;
            genericRepository.detach(baseEntity);
            baseEntity.flatEntity();
        }
    }

    @Autowired
    public void setGenericRepository(GenericRepository<BaseEntity> genericRepository) {
        this.genericRepository = genericRepository;
    }
}