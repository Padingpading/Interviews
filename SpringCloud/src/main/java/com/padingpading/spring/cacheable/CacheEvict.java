package com.padingpading.spring.cacheable;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * pring cache 也支持使用 @CacheEvict 来删除缓存。@CacheEvict 就是一个触发器，在每次调用被它注解的方法时，就会触发删除它指定的缓存的动作。
 * 跟 @Cacheable 和 @CachePut 一样，@CacheEvict 也要求指定一个或多个缓存，也指定自定义一的缓存解析器和 key 生成器，也支持指定条件（condition 参数）
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheEvict {
    @AliasFor("cacheNames")
    String[] value() default {};
    
    @AliasFor("value")
    String[] cacheNames() default {};
    
    String key() default "";
    
    String keyGenerator() default "";
    
    String cacheManager() default "";
    
    String cacheResolver() default "";
    
    String condition() default "";
    
    boolean allEntries() default false;
    
    boolean beforeInvocation() default false;
}
