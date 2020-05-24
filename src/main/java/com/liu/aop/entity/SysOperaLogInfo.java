package com.liu.aop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 日志接口参数实体类
 * @author Jack.Cheng
 * @date 2020/1/7 13:30
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SysOperaLogInfo implements Serializable {
    //日志描述
    private String desc;
    //日志级别
    private int level;
    //注解方法的参数
    private String params;
    //调用方法名
    private String requestMethod;
}
