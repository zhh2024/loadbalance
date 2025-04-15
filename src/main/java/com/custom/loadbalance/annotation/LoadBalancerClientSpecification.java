package com.custom.loadbalance.annotation;

import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Desc: 实现springCloud提供的接口,相当于实体类,存放的是LoadBalancerClient注解中的name和configuration
 * @Author：zhh
 * @Date：2025/4/9 11:17
 */
public class LoadBalancerClientSpecification  {

    private String name;

    private Class<?>[] configuration;

    public LoadBalancerClientSpecification() {
    }

    public LoadBalancerClientSpecification(String name, Class<?>[] configuration) {
        Assert.hasText(name, "name must not be empty");
        this.name = name;
        Assert.notNull(configuration, "configuration must not be null");
        this.configuration = configuration;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        Assert.hasText(name, "name must not be empty");
        this.name = name;
    }


    public Class<?>[] getConfiguration() {
        return this.configuration;
    }

    public void setConfiguration(Class<?>[] configuration) {
        Assert.notNull(configuration, "configuration must not be null");
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        ToStringCreator to = new ToStringCreator(this);
        to.append("name", this.name);
        to.append("configuration", this.configuration);
        return to.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoadBalancerClientSpecification that = (LoadBalancerClientSpecification) o;
        return Objects.equals(this.name, that.name) && Arrays.equals(this.configuration, that.configuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.configuration);
    }

}
