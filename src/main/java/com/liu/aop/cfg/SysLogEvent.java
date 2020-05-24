package com.liu.aop.cfg;

import com.liu.aop.entity.SysLog;
import org.springframework.context.ApplicationEvent;

/**
 * 系统日志事件
 * @author Jack.Cheng
 * @date 2020/1/7 15:04
 **/
public class SysLogEvent extends ApplicationEvent {

    public SysLogEvent(SysLog sysLog) {
        super(sysLog);
    }
}
