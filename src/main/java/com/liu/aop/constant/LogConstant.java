package com.liu.aop.constant;

/**
 * 日志常量
 * @author Jack.Cheng
 * @date 2020/3/19 11:04
 **/
public class LogConstant {

    //系统日志正常入库
    public static final int SYS_NORMAL_HANDLE = 1;

    //处理异常系统日志入库
    public static final int SYS_ERROR_HANDLE = 2;

    //处理成功
    public static final int SUCCESS = 1;

    //处理失败
    public static final int FAILED = 2;

    /**
     * 用户群计算
     */
    public static final int LOG_USER_COUNT = 1;
    /**
     * 用户群入库
     */
    public static final int LOG_USER_INSERT = 2;

    /**
     * 外呼
     */
    public static final int COUNT_HANDLE_CALL = 1;
    /**
     * 短信
     */
    public static final int COUNT_HANDLE_MSG = 2;
}
