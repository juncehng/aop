package com.liu.aop.cfg.circleDependces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 循环依赖
 * @author: Jack.Cheng
 * @date: 2021/3/9 10:48
 */
@Service
public class CircleB {

    @Autowired
    private CircleA circleA;

    public CircleB() {
        System.out.println("CircleB");
    }
}
