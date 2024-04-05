package com.example.my_theatre.config;

import com.example.my_theatre.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.my_theatre.properties.AliOSSProperties;
/*
 * 配置类：用来创建AliOssutil对象
 * */
@Configuration//该注解表明下面的方法是一个配置类
@Slf4j
public class OssConfiguration {
    @Bean
    @ConditionalOnMissingBean//加上这个注解后，整个spring框架中就始终只会有一个aliOssUtil对象
    public AliOssUtil aliOssUtil(AliOSSProperties aliOssProperties) {
        log.info("开始创建阿里云文件上传工具类对象{}",aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());

    }
}
