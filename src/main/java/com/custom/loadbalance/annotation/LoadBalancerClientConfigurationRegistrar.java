package com.custom.loadbalance.annotation;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Desc: 获取LoadBalancerClient注解传入的name和 configuration,放入LoadBalancerClientSpecification对象并注册到容器中
 * @Author：zhh
 * @Date：2025/4/9 11:04
 */
public class LoadBalancerClientConfigurationRegistrar implements ImportBeanDefinitionRegistrar {
    private static String getClientName(Map<String, Object> client) {
        if (client == null) {
            return null;
        }
        String name = (String) client.get("name");
        if (StringUtils.hasText(name)) {
            return name;
        }
        throw new IllegalStateException("Either 'name'  must be provided in @LoadBalancerClient");
    }

    private static void registerClientConfiguration(BeanDefinitionRegistry registry, Object name,
                                                    Object configuration) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                .genericBeanDefinition(LoadBalancerClientSpecification.class);
        builder.addConstructorArgValue(name);
        builder.addConstructorArgValue(configuration);
        registry.registerBeanDefinition(name + ".LoadBalancerClientSpecification", builder.getBeanDefinition());
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        Map<String, Object> client = metadata.getAnnotationAttributes(LoadBalancerClient.class.getName(), true);
        String name = getClientName(client);
        if (name != null) {
            registerClientConfiguration(registry, name, client.get("configuration"));
        }
    }
}
