package com.liu.aop.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * @program: channelh5
 * @description: 查询接口返回对象
 * @author: jack.Cheng
 * @create: 2019-11-18 10:20
 **/
@Data
@ToString
public class ResultObject<T> implements Serializable {

    //结果状态true：成功   false：失败
    private boolean resultStatus;
    //状态码
    private String resultCode;
    //结果对象
    private T result;

    //返回成功信息
    public static <T> ResultObject<T> ok() {
        ResultObject<T> resultObject = new ResultObject<>();
        resultObject.setResultStatus(true);
        resultObject.setResultCode("success");
        return resultObject;
    }

    public static <T> ResultObject<T> ok(T object) {
        ResultObject<T> resultObject = new ResultObject<>();
        resultObject.setResultStatus(true);
        resultObject.setResultCode("success");
        resultObject.setResult(object);
        return resultObject;
    }

    //返回信息失败
    public static <T> ResultObject failed(T result) {
        ResultObject<T> resultObject = new ResultObject<>();
        resultObject.setResultStatus(false);
        resultObject.setResultCode("failed");
        resultObject.setResult(result);
        return resultObject;
    }
}
