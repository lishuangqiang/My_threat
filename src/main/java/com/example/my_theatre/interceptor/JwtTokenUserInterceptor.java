package com.example.my_theatre.interceptor;


import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.constants.JwtClaimsConstant;
import com.example.my_theatre.properties.JwtProperties;
import com.example.my_theatre.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            log.info("User当前拦截到的不是动态方法，直接放行");
            return true;
        }
        log.info("被拦截器 拦截");


        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            int   userId = (int) claims.get(JwtClaimsConstant.USER_ID);
            log.info("当前用户账号id：{}", userId);
            //使用ThreadLocal来存储empID，使得和这个变量在同线程下的其他包可以使用

            BaseContext.setCurrentId((long) userId);
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            log.info("jwt令牌获取失败");
            //返回状态码，前端获取到该状态码之后跳转到登录界面。
            response.setStatus(401);
            return false;
        }
    }
}
