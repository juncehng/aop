package com.liu.aop.cfg.circleDependces;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 循环依赖
 * @author: Jack.Cheng
 * @date: 2021/3/9 10:48
 */
@Service
public class CircleB implements InitializingBean {

    @Autowired
    private CircleA circleA;

    public CircleB() {
        System.out.println("CircleB construct...");
    }

    @PostConstruct
    public void PostConstructMethod(){
        System.out.println("CircleB PostConstructMethod...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("CircleB afterPropertiesSet...");
    }

    public String testMethod(){
        return "CircleB testMethod......";
    }
}
