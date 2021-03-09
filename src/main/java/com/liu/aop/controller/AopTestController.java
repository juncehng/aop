package com.liu.aop.controller;

import com.liu.aop.aspect.annotation.SysOperaLog;
import com.liu.aop.entity.ResultObject;
import com.liu.aop.entity.request.BodyValidateRequest;
import com.liu.aop.entity.request.RequestBodyValidateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

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

    @GetMapping("testValidate")
    public String testValidate(@Validated @NotEmpty(message = "userId不能为空")String userId){
        return userId;
    }

    @GetMapping("testValidate1")
    public String testValidate1(@Validated BodyValidateRequest request){
        return request.toString();
    }

    @PostMapping("testValidate2")
    public String testValidate2(@Validated @RequestBody RequestBodyValidateRequest requestBodyValidateRequest){
        return requestBodyValidateRequest.toString();
    }
}
