package com.padingpading.spring.cacheable;

import org.springframework.aot.hint.annotation.Reflective;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 在执行方法前不会去检查缓存中是否存在key的缓存，每次都会执行该方法，并将执行结果存入指定key的缓存中
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Reflective
public @interface CachePut {
    @AliasFor("cacheNames")
    String[] value() default {};
    
    @AliasFor("value")
    String[] cacheNames() default {};
    
    String key() default "";
    
    String keyGenerator() default "";
    
    String cacheManager() default "";
    
    String cacheResolver() default "";
    
    String condition() default "";
    
    String unless() default "";
}
