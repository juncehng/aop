package com.liu.aop.cfg;

import com.liu.aop.cfg.circleDependces.CircleA;
import com.liu.aop.cfg.circleDependces.CircleB;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author: Jack.Cheng
 * @date: 2021/3/11 17:54
 */
@Component
public class TestBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof CircleA){
            System.out.println("TestBeanPostProcessor CircleA postProcessBeforeInitialization");
        }
        if (bean instanceof CircleB){
            System.out.println("TestBeanPostProcessor CircleB postProcessBeforeInitialization");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof CircleA){
            System.out.println("TestBeanPostProcessor CircleA postProcessAfterInitialization");
        }
        if (bean instanceof CircleB){
            System.out.println("TestBeanPostProcessor CircleB postProcessAfterInitialization");
        }
        return bean;
    }
}
