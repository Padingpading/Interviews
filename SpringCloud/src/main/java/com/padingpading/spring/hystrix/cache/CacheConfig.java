package com.padingpading.spring.hystrix.cache;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author libin
 * @description
 * @date 2022/9/18
 */
@Configuration
public class CacheConfig {
    @Bean
    public FilterRegistrationBean indexFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new HystrixRequestContextFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }
}
