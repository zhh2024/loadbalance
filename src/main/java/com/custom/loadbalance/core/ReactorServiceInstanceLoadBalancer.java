package com.custom.loadbalance.core;

import org.springframework.cloud.client.ServiceInstance;

import org.springframework.cloud.client.loadbalancer.Response;


/**
 * @Desc:
 * @Author：zhh
 * @Date：2025/4/10 10:04
 */
public interface ReactorServiceInstanceLoadBalancer {
    Response<ServiceInstance> choose();
}
