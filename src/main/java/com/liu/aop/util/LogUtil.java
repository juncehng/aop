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

    public static SysOperaLogInfo getSysOperateLogInfo(JoinPoint point) throws Exception {
        String targetName = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        Object[] args = point.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && isParameterTypesEquals(method.getParameterTypes(), args)) {
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
        return null;
    }

    private static boolean isParameterTypesEquals(Class<?>[] parameterTypeClazz, Object[] parameters) {
        if (parameterTypeClazz == null) {
            return false;
        }
        if (parameterTypeClazz.length != parameters.length) {
            return false;
        }
        for (int i = 0; i < parameterTypeClazz.length; i++) {
            if (!parameterTypeClazz[i].getName().equals(parameters[i].getClass().getName())) {
                return false;
            }
        }
        return true;
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
}
