package com.liu.aop.entity.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 直接使用对象做参数验证@Validate注解
 * @author: Jack.Cheng
 * @date: 2021/3/9 16:41
 */
@Data
public class RequestBodyValidateRequest {

    @NotEmpty(message = "userId不能为空")
    private String userId;

    @Override
    public String toString() {
        return "RequestBodyValidateRequest{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
