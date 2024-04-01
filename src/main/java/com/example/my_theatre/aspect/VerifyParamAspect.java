package com.example.my_theatre.aspect;


import com.example.my_theatre.annotation.VerifyParam;
import com.example.my_theatre.entity.dto.UserDto;
import com.example.my_theatre.entity.enums.VerifyRegexEnum;
import com.example.my_theatre.utils.VerifyRegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

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
    @Pointcut("@annotation(com.example.my_theatre.annotation.VerifyParam)")
    public void autoVerifyPointCut() {
    }

    /*
     * 前置通知，在通知中进行公共字段的赋值
     * */
    @Before("autoVerifyPointCut() && @annotation(verifyParam)")
    public void autojudje(JoinPoint joinPoint, VerifyParam verifyParam) throws NoSuchMethodException {
        log.info("开始参数的校验");
        // 获取方法参数
        Object[] args = joinPoint.getArgs();

        // 获取方法
        String userName = null;
        String email=null;
        String password=null;
        if (args.length > 0 && args[0] instanceof UserDto) {
            UserDto userinfo = (UserDto) args[0];

            email = userinfo.getEmail();
            userName = userinfo.getUserName();
            password = userinfo.getPassword();
        }
        // 遍历参数数组，逐个校验参数

        // 校验姓名
        if (verifyParam.regexName() != VerifyRegexEnum.NO && userName instanceof String) {

            if (!VerifyRegexUtils.VerifyName(userName)) {
                throw new IllegalArgumentException("姓名参数校验失败");
            }
        }
        // 校验邮箱
        if (verifyParam.regexAccount() != VerifyRegexEnum.NO && email instanceof String) {

            if (!VerifyRegexUtils.VerifyEmail(email)) {
                throw new IllegalArgumentException("邮箱参数校验失败");
            }
        }
        // 校验密码
        if (verifyParam.regexPassword() != VerifyRegexEnum.NO && password instanceof String) {

            if (!VerifyRegexUtils.VerifyPassword(password)) {
                throw new IllegalArgumentException("密码参数校验失败");
            }
        }
        // 校验账户
        if (verifyParam.regexAccount() != VerifyRegexEnum.NO && password instanceof String) {

            if (!VerifyRegexUtils.VerifyAccount(password)) {
                throw new IllegalArgumentException("密码参数校验失败");
            }
        }
    }

}
