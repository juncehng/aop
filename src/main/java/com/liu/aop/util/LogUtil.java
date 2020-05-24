package com.liu.aop.util;

import com.alibaba.fastjson.JSON;
import com.liu.aop.aspect.annotation.SysOperaLog;
import com.liu.aop.entity.SysOperaLogInfo;
import org.aspectj.lang.JoinPoint;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Random;

/**
 * 日志工具类
 * @author Jack.Cheng
 * @date 2020/1/7 13:28
 **/
public class LogUtil {

    /***
     * 获取切点注解信息
     * @param point AOP切面信息
     * @return 注解信息对应实体类
     */
    public static SysOperaLogInfo getSysOperateLogInfo(JoinPoint point) throws Exception {
        //获取连接点目标类名，切点所属类（这里为注解方法所对应的controller类）
        String targetName = point.getTarget().getClass().getName();
        //获取连接点签名的方法名（这里为加注解的方法）
        String methodName = point.getSignature().getName();
        //获取连接点参数（切点方法参数）
        Object[] args = point.getArgs();
        //根据连接点类的名字获取指定类（实例化对象)
        Class targetClass = Class.forName(targetName);
        //获取类里面的方法（注解对应类中的所有方法）
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                //可能出现同方法名方法，当方法名及其参数个数都相等时，视为同一方法
                Class[] parameterTypeClazzArr = method.getParameterTypes();
                if (parameterTypeClazzArr.length == args.length) {
                    for (int i = 0; i < args.length; i++) {
                        if(args[i] instanceof HttpSession){
                            args[i] = "session";
                            break;
                        }
                    }
                    return new SysOperaLogInfo(
                            method.getAnnotation(SysOperaLog.class).desc(),
                            method.getAnnotation(SysOperaLog.class).level(),
                            JSON.toJSONString(args),
                            methodName
                    );
                }
            }
        }
        return null;
    }

    /**
     * 生成32位数的订单Id号 yyyyMMddhhmmss + 2位分库号 + 6为末端IP号 + 5位CSF端口号 + 5位随机数
     * @return 分库号  CSF端口号  IP号先用随机数代替
     */
    public static String randomLogId(){
        return TimeUtils.getCurrentDateString(TimeUtils.DATE_TIME_FORMAT_SQL) +
                randomNum(2) +
                randomNum(6) +
                randomNum(5) +
                randomNum(5);
    }

    /**
     * 生成随机数字
     * @param len 生成随机数长度
     * @return 随机数
     */
    private static String randomNum(int len){
        String source = "0123456789";
        Random random = new Random();
        StringBuilder buffer = new StringBuilder();
        for(int i=0; i<len; i++){
            buffer.append(source.charAt(random.nextInt(9)));
        }
        return buffer.toString();
    }

//    /**
//     * 获取堆栈信息
//     */
//    public static String getStackTrace(Throwable throwable) {
//        StringWriter sw = new StringWriter();
//        try (PrintWriter pw = new PrintWriter(sw)) {
//            throwable.printStackTrace(pw);
//            return sw.toString();
//        }
//    }
}
