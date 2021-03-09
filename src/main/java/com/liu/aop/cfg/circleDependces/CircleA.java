package com.liu.aop.cfg.circleDependces;

import com.liu.aop.aspect.annotation.SysOperaLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 循环依赖测试
 * @author: Jack.Cheng
 * @date: 2021/3/9 10:47
 */
@Service
public class CircleA {

    @Autowired
    private CircleB circleb;

    @SysOperaLog(desc = "循环依赖时，代理对象测试")
    public void proxyCircleTestMethod(){

    }

    public CircleA() {
        System.out.println("CircleA");
    }
}
