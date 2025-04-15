package com.custom.loadbalance.annotation;

import com.custom.loadbalance.core.RandomLoadBalancer;
import com.custom.loadbalance.core.ReactorServiceInstanceLoadBalancer;
import com.custom.loadbalance.core.ServiceInstanceListSupplier;
import com.custom.loadbalance.support.LoadBalancerClientFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @Desc:
 *  不在spring.factories,不会被spring主容器加载,会被创建的子容器加载
 *  默认负载均衡配置,如果服务没有使用LoadBalancerClient自定义负载均衡配置,需要默认的负载均衡
 * @Author：zhh
 * @Date：2025/4/9 11:38
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnDiscoveryEnabled
public class LoadBalancerClientConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public ReactorServiceInstanceLoadBalancer reactorServiceInstanceLoadBalancer(
            Environment environment,
            ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new RandomLoadBalancer(discoveryClientServiceInstanceListSupplier,name);
    }


    /**
     * 注入DiscoveryClientServiceInstanceListSupplier, 执行构造函数,获取服务实例
     */
    @Bean
    @ConditionalOnBean(DiscoveryClient.class)
    @ConditionalOnMissingBean
    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
            ConfigurableApplicationContext context) {
        return ServiceInstanceListSupplier.builder().withBlockingDiscoveryClient().build(context);
    }

}
