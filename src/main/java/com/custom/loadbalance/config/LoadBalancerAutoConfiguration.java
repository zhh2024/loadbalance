package com.custom.loadbalance.config;

import com.custom.loadbalance.annotation.LoadBalancerClientSpecification;
import com.custom.loadbalance.client.BlockingLoadBalancerClient;
import com.custom.loadbalance.support.LoadBalancerClientFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerBeanPostProcessorAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Desc: SpringStarter自动配置类
 * @Author：zhh
 * @Date：2025/4/9 11:24
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({ ReactorLoadBalancerClientAutoConfiguration.class,
        LoadBalancerBeanPostProcessorAutoConfiguration.class })
@ConditionalOnProperty(value = "spring.cloud.loadbalancer.enabled", havingValue = "true", matchIfMissing = true)
public class LoadBalancerAutoConfiguration {

    private final List<LoadBalancerClientSpecification>  configurations;

    /**
     * 构造函数接收LoadBalancerClientConfigurationRegistrar注册进来的LoadBalancerClientSpecification
     * @param configurations
     */
    public LoadBalancerAutoConfiguration(List<LoadBalancerClientSpecification> configurations) {
        this.configurations = configurations;
    }

    @ConditionalOnMissingBean
    @Bean
    public LoadBalancerClientFactory loadBalancerClientFactory() {
        LoadBalancerClientFactory clientFactory = new LoadBalancerClientFactory();
        //将配置放入clientFactory中
        clientFactory.setConfigurations(this.configurations);
        return clientFactory;
    }

    @Bean
    @ConditionalOnBean(LoadBalancerClientFactory.class)
    @ConditionalOnMissingBean
    public BlockingLoadBalancerClient blockingLoadBalancerClient(LoadBalancerClientFactory loadBalancerClientFactory) {
        return new BlockingLoadBalancerClient(loadBalancerClientFactory);
    }
}
