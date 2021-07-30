package com.liu.aop.cfg.circleDependces;

import com.liu.aop.aspect.annotation.SysOperaLog;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 循环依赖测试
 * @author: Jack.Cheng
 * @date: 2021/3/9 10:47
 */
@Service
public class CircleA implements InitializingBean {

    @Autowired
    private CircleB circleb;

    @SysOperaLog(desc = "循环依赖时，代理对象测试")
    public void proxyCircleTestMethod(){

    }

    @PostConstruct
    public void PostConstructMethod(){
        System.out.println("CircleA PostConstructMethod...");
    }

    public CircleA() {
        System.out.println("CircleA construct...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("CircleA afterPropertiesSet...");
    }

    public void testMethodA(){
        System.out.println("调用methodA的方法：" + circleb.testMethod());
    }
}
