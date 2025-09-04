package com.padingpading.spring.cacheable;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * https://www.cnblogs.com/coding-one/p/12401630.html
 * https://maimai.cn/article/detail?fid=1796368129&efid=zNDAhqtB6AnRM0SH1yXvCA
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cacheable {

    //指定缓存组件的名字;将方法的返回结果放在哪个缓存中，是数组的方式，可以指定多个缓
    @AliasFor("cacheNames")
    String[] value() default {};

    //指定缓存组件的名字;将方法的返回结果放在哪个缓存中，是数组的方式，可以指定多个缓
    @AliasFor("value")
    String[] cacheNames() default {};

    //key的生成器；可以自己指定key的生成器的组件id
    String key() default "";

    String keyGenerator() default "";

    // 指定缓存管理器；或者cacheResolver指定获取解析器
    String cacheManager() default "";

    //
    String cacheResolver() default "";

    /**
     * 缓存的条件，可以是一个 SpEL 表达式，表示缓存的结果是否应该被缓存。默认值为一个空字符串，表示不考虑任何条件，缓存所有结果。
     */
    String condition() default "";

    /**
     *  缓存的排除条件，可以是一个 SpEL 表达式，表示缓存的结果是否应该被排除在缓存之外。默认值为一个空字符串，表示不排除任何结果。
     */
    String unless() default "";

    /**
     * 是否同步，true/false。在一个多线程的环境中，某些操作可能被相同的参数并发地调用，这样同一个 value 值可能被多次计算（或多次访问 db），这样就达不到缓存的目的。
     * 针对这些可能高并发的操作，我们可以使用 sync 参数来告诉底层的缓存提供者将缓存的入口锁住，这样就只能有一个线程计算操作的结果值，而其它线程需要等待，这样就避免了 n-1 次数据库访问。
     */
    boolean sync() default false;
}
