package com.custom.loadbalance.client;

import org.springframework.cloud.client.ServiceInstance;


/**
 * @Desc: 客户端接口
 * @Author：zhh
 * @Date：2025/4/10 10:45
 */
public interface LoadBalancerClient {

    ServiceInstance choose(String serviceId);

}
