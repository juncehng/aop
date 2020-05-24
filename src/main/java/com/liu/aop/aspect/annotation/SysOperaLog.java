package com.liu.aop.aspect.annotation;

import java.lang.annotation.*;

/**
 * 主要标注日志的具体用处也就是具体操作
 * @author Jack.Cheng
 * @date 2020/1/7 10:05
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface SysOperaLog {
    //描述，默认为空
    String desc() default "" ;
    //日志级别，默认为一般  级别：5-严重、4-警告、3-敏感、2-重要、1-一般
    int level() default 1;
}
