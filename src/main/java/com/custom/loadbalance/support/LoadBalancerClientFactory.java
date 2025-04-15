package com.custom.loadbalance.support;


import com.custom.loadbalance.annotation.LoadBalancerClientConfiguration;
import com.custom.loadbalance.annotation.LoadBalancerClientSpecification;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Desc: 为每个服务创建独立的子容器
 * @Author：zhh
 * @Date：2025/4/9 11:29
 */
public class LoadBalancerClientFactory implements ApplicationContextAware {

    public static final String NAMESPACE = "loadbalancer";

    public static final String PROPERTY_NAME = NAMESPACE + ".client.name";

    private final Map<String, AnnotationConfigApplicationContext> contexts = new ConcurrentHashMap<>();

    private Map<String,LoadBalancerClientSpecification> configurations = new ConcurrentHashMap<>();

    /**
     * 父容器
     */
    private ApplicationContext parent;

    /**
     * 默认配置
     */
    private Class<?> defaultConfigType;

    public LoadBalancerClientFactory() {
        this.defaultConfigType = LoadBalancerClientConfiguration.class;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.parent = applicationContext;
    }

    public void setConfigurations(List<LoadBalancerClientSpecification> configurations) {
        for (LoadBalancerClientSpecification config: configurations){
            this.configurations.put(config.getName(),config);
        }
    }


    public <T> T getInstance(String name, Class<T> type) {
        AnnotationConfigApplicationContext context = getContext(name);
        try {
            return context.getBean(type);
        }
        catch (NoSuchBeanDefinitionException e) {
            // ignore
        }
        return null;

    }
    protected AnnotationConfigApplicationContext getContext(String name) {
        if (!this.contexts.containsKey(name)) {
            synchronized (this.contexts) {
                if (!this.contexts.containsKey(name)) {
                    this.contexts.put(name, createContext(name));
                }
            }
        }
        return this.contexts.get(name);
    }

    protected AnnotationConfigApplicationContext createContext(String name) {
        AnnotationConfigApplicationContext context;
        if (this.parent != null) {
            DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
            if (parent instanceof ConfigurableApplicationContext) {
                beanFactory.setBeanClassLoader(
                        ((ConfigurableApplicationContext) parent).getBeanFactory().getBeanClassLoader());
            }
            else {
                beanFactory.setBeanClassLoader(parent.getClassLoader());
            }
            context = new AnnotationConfigApplicationContext(beanFactory);
            context.setClassLoader(this.parent.getClassLoader());
        }
        else {
            context = new AnnotationConfigApplicationContext();
        }
        //注册自定义配置
        if (this.configurations.containsKey(name)) {
            for (Class<?> configuration : this.configurations.get(name).getConfiguration()) {
                context.register(configuration);
            }
        }
        for (Map.Entry<String, LoadBalancerClientSpecification> entry : this.configurations.entrySet()) {
            if (entry.getKey().startsWith("default.")) {
                for (Class<?> configuration : entry.getValue().getConfiguration()) {
                    context.register(configuration);
                }
            }
        }
        //注册默认配置
        context.register(PropertyPlaceholderAutoConfiguration.class, this.defaultConfigType);
        //子容器存入服务名称变量
        context.getEnvironment().getPropertySources().addFirst(new MapPropertySource(NAMESPACE,
                Collections.<String, Object>singletonMap(PROPERTY_NAME, name)));
        if (this.parent != null) {
            context.setParent(this.parent);
        }
        context.setDisplayName(generateDisplayName(name));
        //刷新Spring容器
        context.refresh();
        return context;
    }
    protected String generateDisplayName(String name) {
        return this.getClass().getSimpleName() + "-" + name;
    }
}
