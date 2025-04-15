package com.custom.loadbalance.core;

import org.springframework.cloud.client.ServiceInstance;


import java.util.List;

/**
 * @Desc:
 * @Author：zhh
 * @Date：2025/4/10 9:38
 */
public interface ServiceInstanceListSupplier {

    String getServiceId();

    List<ServiceInstance>get();

    static ServiceInstanceListSupplierBuilder builder() {
        return new ServiceInstanceListSupplierBuilder();
    }

}
