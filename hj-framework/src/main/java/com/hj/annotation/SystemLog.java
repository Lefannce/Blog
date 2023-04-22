package com.hj.annotation;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
     * 定义注解
     */
@Retention(RetentionPolicy.RUNTIME) //在运行时
@Target(ElementType.METHOD)         //可以加到方法上
public @interface SystemLog {

    String businessName();//添加属性后可以在controller使用注解指定名称


}
