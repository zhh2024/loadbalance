package com.custom.loadbalance.core;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @Desc:
 * @Author：zhh
 * @Date：2025/4/10 9:39
 */
public class ServiceInstanceListSupplierBuilder {

    public interface Creator extends Function<ConfigurableApplicationContext, ServiceInstanceListSupplier> {

    }

    public interface DelegateCreator extends
            BiFunction<ConfigurableApplicationContext, ServiceInstanceListSupplier, ServiceInstanceListSupplier> {

    }

    private Creator baseCreator;

    private DelegateCreator cachingCreator;

    private final List<DelegateCreator> creators = new ArrayList<>();

    ServiceInstanceListSupplierBuilder() {
    }

    public ServiceInstanceListSupplierBuilder withBlockingDiscoveryClient() {

        this.baseCreator = context -> {
            DiscoveryClient discoveryClient = context.getBean(DiscoveryClient.class);
            return new DiscoveryClientServiceInstanceListSupplier(discoveryClient, context.getEnvironment());
        };
        return this;
    }

    public ServiceInstanceListSupplier build(ConfigurableApplicationContext context) {
        Assert.notNull(baseCreator, "A baseCreator must not be null");

        ServiceInstanceListSupplier supplier = baseCreator.apply(context);

        for (DelegateCreator creator : creators) {
            supplier = creator.apply(context, supplier);
        }

        if (this.cachingCreator != null) {
            supplier = this.cachingCreator.apply(context, supplier);
        }
        return supplier;
    }
}
