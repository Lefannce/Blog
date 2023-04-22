package com.hj.aspect;


import com.alibaba.fastjson.JSON;
import com.hj.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 *切面类(难点,没学懂)
 */

@Component  //注入容器
@Aspect     //标识为切面类
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.hj.annotation.SystemLog)")      //指定切点,加入了SystemLog的方法都会去打印日志
    public void pt(){

    }


    @Around("pt()")     //环绕通知,切点为pt()
    public Object printLog(ProceedingJoinPoint JoinPoint) throws Throwable { //相当于被增强的那个方法的信息封装的对象
        //不能直接try catch ,因为这样会把controller的异常全部拦截掉,使统一异常处理时效
        Object proceed;
        try {
            handleBefore(JoinPoint);     //执行方法之前打印的
            proceed = JoinPoint.proceed();      //相当于调用目标方法获取参数
            handleAfter(proceed);      // 执行方法之后打印的
        } finally {
        // 结束后换行
        log.info("=======End=======" + System.lineSeparator()); //拼接换行符
        }
        return proceed;
    }



    //TODO 73集第17分钟
    private void handleAfter(Object proceed) {
         // 打印出参
        log.info("Response       : {}",JSON.toJSONString(proceed));


    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {
        //获取请求头强转为ServletRequestAttributes
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //获取被增强方法上的注解对象
        SystemLog systemLog = getSystemLog(joinPoint);

        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}",systemLog.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}",joinPoint.getSignature().getDeclaringType(),((MethodSignature)joinPoint.getSignature()).getName());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSON(joinPoint.getArgs()));
    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();//获取签名信息
        SystemLog systemLog = methodSignature.getMethod().getAnnotation(SystemLog.class);//获取反射中的getMethod对象
        return systemLog;

    }


}
