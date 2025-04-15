package com.custom.loadbalance.core;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;

import java.time.Duration;
import java.util.List;

import static com.custom.loadbalance.support.LoadBalancerClientFactory.PROPERTY_NAME;

/**
 * @Desc: 构造函数调用DiscoveryClient实现类, 通过serviceId获取serviceInstances
 * @Author：zhh
 * @Date：2025/4/10 9:40
 */
public class DiscoveryClientServiceInstanceListSupplier implements ServiceInstanceListSupplier {

    private Duration timeout = Duration.ofSeconds(30);

    private final String serviceId;

    private final List<ServiceInstance> serviceInstances;

    public DiscoveryClientServiceInstanceListSupplier(DiscoveryClient delegate, Environment environment) {
        this.serviceId = environment.getProperty(PROPERTY_NAME);
        this.serviceInstances = delegate.getInstances(serviceId);
    }

    @Override
    public List<ServiceInstance> get() {
        return serviceInstances;
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

}
