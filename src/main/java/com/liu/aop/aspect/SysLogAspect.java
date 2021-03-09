package com.liu.aop.aspect;

import cn.hutool.core.convert.Convert;
import com.liu.aop.cfg.SysLogEvent;
import com.liu.aop.constant.LogConstant;
import com.liu.aop.entity.ResultObject;
import com.liu.aop.entity.SysLog;
import com.liu.aop.entity.SysOperaLogInfo;
import com.liu.aop.util.CommonUtil;
import com.liu.aop.util.LogUtil;
import com.liu.aop.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Objects;

/**
 * 系统日志切面
 * @author Jack.Cheng
 * @date 2020/1/7 10:22
 **/
@Slf4j
@Aspect
@Component
public class SysLogAspect {

    /**
     * 用于保存当前线程调用接口的日志信息，贯穿AOP各个通知方法的生命周期
     */
    private static final ThreadLocal<SysLog> LOCAL_SYS_LOG = new ThreadLocal<>();

    /**
     * SpringIoc容器，使用Spring的事件监听机制，发布日志消息事件，异步处理日志信息
     * 家庭条件好的，这里可以用MQ队列来实现日志的异步消费 0.0
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 定义AOP切点信息，拦截所有带有@SysOperaLog注解的方法
     */
    @Pointcut("@annotation(com.liu.aop.aspect.annotation.SysOperaLog)")
    public void sysLogAspect() { }

    /**
     * 前置通知，在目标方法被调用之前调用通知功能，通知方法会在目标方法调用之前执行。
     * @param joinPoint 切点信息
     */
    @Before(value = "sysLogAspect()")
    public void recordLog(JoinPoint joinPoint){
        SysLog sysLog = new SysLog();
        try {
            // 记录日志处理
            long beginTime = Instant.now().toEpochMilli();
            HttpServletRequest request = getRequest();
            sysLog.setLogId(LogUtil.randomLogId())
                  .setCreateTime(TimeUtils.getCurrentDateString(TimeUtils.DATE_TIME_FORMAT))
                  .setIp(CommonUtil.getIpAddr(request))
                  .setLogDate(TimeUtils.getCurrentDateString(TimeUtils.DATE_DAY_FORMAT));

            // 获取方法的@SysOperaLog注解信息，初始化封装日志信息
            SysOperaLogInfo sysOperateLogInfo = LogUtil.getSysOperateLogInfo(joinPoint);
            if(null != sysOperateLogInfo){
                sysLog.setDescription(sysOperateLogInfo.getDesc())
                      .setOperateLevel(sysOperateLogInfo.getLevel())
                      .setParams(sysOperateLogInfo.getParams())
                      .setRequestMethod(sysOperateLogInfo.getRequestMethod());
            }
            long endTime = Instant.now().toEpochMilli();
            sysLog.setConsumingTime(endTime - beginTime);
            log.info("接口开始调用执行，当前logInfo:{}",sysLog.toString());
        }catch (Exception e){
            log.error("AOP日志处理失败，ERROR:",e);
        }finally {
            // 使用ThreadLocal保存日志初始化参数，方法执行完或抛出异常后继续封装日志信息
            LOCAL_SYS_LOG.set(sysLog);
        }
    }

    /**
     * 返回通知，在目标方法成功执行之后调用通知，通知方法会在目标方法返回后调用。
     * @param result 接口的执行结果，与注解的result对应
     */
    @AfterReturning(returning = "result", pointcut = "sysLogAspect()")
    public void doAfterReturning(Object result) {
        try {
            //得到当前线程的log对象
            SysLog sysLog = LOCAL_SYS_LOG.get();
            if(null != sysLog){
                ResultObject resultInfo = Convert.convert(ResultObject.class, result);
                if (resultInfo.isResultStatus()) {
                    sysLog.setStatus(LogConstant.SUCCESS);
                } else {
                    sysLog.setStatus(LogConstant.FAILED);
                    String exDesc = resultInfo.getResult() == null ? "" : resultInfo.getResult().toString();
                    sysLog.setExDesc(exDesc);
                }
                //setGridUserInfo(sysLog);
                applicationContext.publishEvent(new SysLogEvent(sysLog));
                log.info("接口执行完成，当前logInfo:{}",sysLog.toString());
            }
        }finally {
            LOCAL_SYS_LOG.remove();
        }
    }

    /**
     * 异常通知，在目标方法抛出异常后或前置通知抛出异常后调用，通知方法会在目标表方法抛出异常后执行。
     * @param e 接口抛出的异常信息
     */
    @AfterThrowing(pointcut = "sysLogAspect()", throwing = "e")
    public void doAfterThrowable(Throwable e) {
        try {
            SysLog sysLog = LOCAL_SYS_LOG.get();
            if(null != sysLog){
                sysLog.setExDesc("系统内部异常");
                //setGridUserInfo(sysLog);
                applicationContext.publishEvent(new SysLogEvent(sysLog));
                log.info("接口执行异常，当前logInfo:{}",sysLog.toString());
            }
        }finally {
            LOCAL_SYS_LOG.remove();
        }
    }

    private HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }


    //    private void setGridUserInfo(SysLog sysLog){
//        HttpServletRequest request = getRequest();
//        HttpSession session = request.getSession();
//
//        String staffCode = Objects.nonNull(session.getAttribute(LoginConstant.LOGIN_PARAM_STAFF_CODE)) ?
//                (String) session.getAttribute(LoginConstant.LOGIN_PARAM_STAFF_CODE) : null;
//
//        String gridId = Objects.nonNull(session.getAttribute(LoginConstant.LOGIN_PARAM_ORG_ID)) ?
//                (String) session.getAttribute(LoginConstant.LOGIN_PARAM_ORG_ID) : null;
//
//        int gridLevel = Objects.nonNull(session.getAttribute(LoginConstant.LOGIN_PARAM_ORG_LEVEL)) ?
//                (Integer) session.getAttribute(LoginConstant.LOGIN_PARAM_ORG_LEVEL) : 0;
//
//        String phoneNo = Objects.nonNull(session.getAttribute(LoginConstant.LOGIN_PARAM_TOKEN)) ?
//                (String) session.getAttribute(LoginConstant.LOGIN_PARAM_TOKEN) : null;
//
//        sysLog.setStaffCode(staffCode)
//                .setGridId(gridId)
//                .setGridLevel(gridLevel)
//                .setPhoneNo(phoneNo);
//    }
}
