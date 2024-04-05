package com.example.my_theatre.interceptor;


import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.constants.JwtClaimsConstant;
import com.example.my_theatre.entity.po.Admin;
import com.example.my_theatre.mapper.AdminMapper;
import com.example.my_theatre.properties.JwtProperties;
import com.example.my_theatre.service.AdminAccountService;
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
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private AdminMapper adminMapper;

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
            log.info("Admin当前拦截到的不是动态方法，直接放行");
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            String  adminId = (String) claims.get(JwtClaimsConstant.ADMIN_ACCOUNT);
            log.info("当前用户账号id：{}", adminId);
            //使用ThreadLocal来存储empID，使得和这个变量在同线程下的其他包可以使用
            //类型转换问题（数据库字段问题）
            //todo(管理员令牌校验，需要检查当前登录的账号是否是管理员)
            Admin admin = adminMapper.getByaccount(adminId);
            if(admin==null)
            {
                log.info("普通用户越权操作");
                //返回状态码，前端获取到该状态码之后跳转到登录界面。
                response.setStatus(401);
                return false;
            }

            BaseContext.setCurrentId(Long.valueOf(adminId));
            //3、通过，放行
            return true;
            //4、不通过，响应401状态码
        } catch (Exception ex) {
            log.info("jwt令牌获取失败");
            //返回状态码，前端获取到该状态码之后跳转到登录界面。
            response.setStatus(401);
            return false;
        }
    }
}
