package com.custom.loadbalance.client;

import com.custom.loadbalance.core.ReactorServiceInstanceLoadBalancer;
import com.custom.loadbalance.support.LoadBalancerClientFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Response;


/**
 * @Desc: 默认阻塞式MVC
 * @Author：zhh
 * @Date：2025/4/10 9:31
 */
public class BlockingLoadBalancerClient implements LoadBalancerClient {

    private final LoadBalancerClientFactory loadBalancerClientFactory;

    public BlockingLoadBalancerClient(LoadBalancerClientFactory loadBalancerClientFactory) {
        this.loadBalancerClientFactory = loadBalancerClientFactory;
    }

    @Override
    public ServiceInstance choose(String serviceId) {
        ReactorServiceInstanceLoadBalancer loadBalancer = loadBalancerClientFactory.getInstance(serviceId, ReactorServiceInstanceLoadBalancer.class);
        if (loadBalancer == null) {
            return null;
        }
        Response<ServiceInstance> loadBalancerResponse = loadBalancer.choose();
        if (loadBalancerResponse == null) {
            return null;
        }
        return loadBalancerResponse.getServer();
    }


}
