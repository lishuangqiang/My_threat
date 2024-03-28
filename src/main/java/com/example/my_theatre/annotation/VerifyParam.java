package com.example.my_theatre.annotation;



import com.alibaba.fastjson.JSONPatch;
import com.example.my_theatre.entity.enums.VerifyRegexEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface VerifyParam {
    /**
     * 基于正则表达式校验姓名
     *
     * @return
     */
    VerifyRegexEnum regexName() default VerifyRegexEnum.NO;

    /**
     * 基于正则表达式校验邮箱
     *
     */
    VerifyRegexEnum regexAccount() default VerifyRegexEnum.NO;


    /**
     * 基于正则表达式校验密码
     *
     */
    VerifyRegexEnum regexPassword() default VerifyRegexEnum.NO;


    boolean required() default false;
}
