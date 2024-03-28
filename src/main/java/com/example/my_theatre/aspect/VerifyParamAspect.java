package com.example.my_theatre.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/*
* 自定义切面，实现参数的校验
* */
@Aspect
@Component
@Slf4j
public class VerifyParamAspect {
    /*
    * 切入点   选择mapper包下带有AutoFill的注解
    * */
    @Pointcut("@annotation(com.example.my_theatre.annotation.VerifyParam)" )
    public void autoVerifyPointCut(){}

    /*
    * 前置通知，在通知中进行公共字段的赋值
    * */
    @Before("autoVerifyPointCut()")
    public void autojudje(JoinPoint joinPoint) throws NoSuchMethodException {
        log.info("开始参数的校验");
        Object[] args = joinPoint.getArgs();
        System.out.println(args);
        //拿到传递过来的参数
        Object arg = args[0];
        Class<?> clazz = arg.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            System.out.println("Field name: " + field.getName());
            System.out.println("Field type: " + field.getType());
            // 可以根据需要进行其他操作，比如获取字段值等
        }



  }

}
