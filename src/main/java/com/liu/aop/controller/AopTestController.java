package com.liu.aop.controller;

import com.liu.aop.aspect.annotation.SysOperaLog;
import com.liu.aop.entity.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第三方创建任务RESTFUL接口
 * @author Jack.Cheng
 * @date 2019/12/26 9:51
 **/
@Slf4j
@RestController
public class AopTestController {

    @GetMapping("testAop")
    @SysOperaLog(desc = "【AOP注解测试接口】",level = 2)
    public ResultObject testMyAop(String msg){
        log.info("【AOP注解测试接口】，msg:{}",msg);
        return ResultObject.ok(msg);
    }
}
