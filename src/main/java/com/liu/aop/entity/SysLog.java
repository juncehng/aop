package com.liu.aop.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 主要记录的信息有: 操作人，操作IP，方法名，参数，消耗时间，日志类型，操作状态【成功/失败】
 * 操作类型(操作日志和异常日志)以及增删改查记录，操作时间等。
 * @author Jack.Cheng
 * @date 2020/1/7 10:08
 **/
@Data
@ToString
@Accessors(chain = true)
@NoArgsConstructor
public class SysLog implements Serializable {
    //主键
    private String logId;
    //操作人工号
    private String staffCode;
    //电话号码
    private String phoneNo;
    //工号对应区域ID
    private String gridId;
    //工号层级
    private int gridLevel;
    //操作状态，0-成功 1-失败
    private Integer status;
    //该操作的级别：5-严重、4-警告、3-敏感、2-重要、1-一般
    private Integer operateLevel;
    //操作IP
    private String ip;
    //操作描述
    private String description;
    //请求参数
    private String params;
    //请求方法
    private String requestMethod;
    //日志创建日 yyyy-MM-dd
    private String logDate;
    //日志创建时间yyyy-MM-dd hh:mm:ss
    private String createTime;
    //消耗时间
    private Long consumingTime;
    //错误描述
    private String exDesc;
}
