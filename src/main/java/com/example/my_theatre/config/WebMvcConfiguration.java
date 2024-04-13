package com.example.my_theatre.config;


import com.example.my_theatre.interceptor.JwtTokenAdminInterceptor;
import com.example.my_theatre.interceptor.JwtTokenUserInterceptor;
import com.example.my_theatre.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;


/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;


    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                //排除以下路径：用户登录，用户注册，用户找回密码，发送验证码
                .excludePathPatterns("/user/account/sendEmailCode")
                .excludePathPatterns("/user/account/register")
                .excludePathPatterns("/user/account/forgetPassword")
                .excludePathPatterns("/user/account/login")
                .excludePathPatterns("/user/account/loginByCode");


        //todo(取消管理员拦截器)
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/account/login");
    }

    /*
     * 通过扩展Spring MVC中的消息转换器，实现对数据格式的统一管理（之前我们采用的是@jsonFormat注解来解决）
     * */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器");
        //创建一个消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //需要为消息转换器指定对象转换器    对象转换器课可以将java对象序列化为Json对象
        converter.setObjectMapper(new JacksonObjectMapper());
        //将自定义的消息转换器加入到容器中
        converters.add(0, converter);

    }


}
