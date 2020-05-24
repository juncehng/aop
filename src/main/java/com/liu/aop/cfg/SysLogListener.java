package com.liu.aop.cfg;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 日志事件监听器，推送系统日志到MQ消息队列中
 * @author Jack.Cheng
 * @date 2020/4/20 9:26
 **/
@Slf4j
@Component
public class SysLogListener implements ApplicationListener<SysLogEvent> {

    @Async
    @Order
    @Override
    public void onApplicationEvent(SysLogEvent event) {
        try {
            log.info("接收到接口日志信息，event:{}",JSON.toJSONString(event.getSource()));
        }catch (Exception e){
            log.info("系统日志数据压入MQ队列失败",e);
        }
    }
}
