package com.custom.loadbalance.annotation;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Desc: 使用了该注解,不同的服务对应不同的负载均衡策略配置
 * @Author：zhh
 * @Date：2025/4/9 11:02
 */
@Configuration(proxyBeanMethods = false)
@Import(LoadBalancerClientConfigurationRegistrar.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoadBalancerClient {

    String name() default "";

    Class<?>[] configuration() default {};
}
