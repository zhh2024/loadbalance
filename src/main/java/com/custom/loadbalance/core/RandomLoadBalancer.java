package com.custom.loadbalance.core;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Response;


import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Desc: 随机负载均衡器
 * @Author：zhh
 * @Date：2025/4/10 10:25
 */
public class RandomLoadBalancer implements ReactorServiceInstanceLoadBalancer{

    private final String serviceId;

    private ServiceInstanceListSupplier serviceInstanceListSupplierProvider;


    public RandomLoadBalancer(ServiceInstanceListSupplier serviceInstanceListSupplierProvider,
                              String serviceId) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Response<ServiceInstance> choose() {
        return processInstanceResponse(serviceInstanceListSupplierProvider,serviceInstanceListSupplierProvider.get());
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances) {
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances);

        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            return new EmptyResponse();
        }
        int index = ThreadLocalRandom.current().nextInt(instances.size());

        ServiceInstance instance = instances.get(index);

        return new DefaultResponse(instance);
    }
}
