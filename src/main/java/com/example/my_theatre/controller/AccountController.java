package com.example.my_theatre.controller;


import com.example.my_theatre.common.BaseResponse;
import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.entity.constants.EmailConstant;
import com.example.my_theatre.entity.dto.Userinfo;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.vo.UserinfoVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.service.impl.UserAccountServiceImpl;
import com.example.my_theatre.utils.VerifyRegexUtils;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user/account")
public class AccountController {
    @Resource
    UserAccountServiceImpl userAccountService;
    /**
     * 发送邮箱验证码
     */
    @PostMapping("/sendEmailCode")
    public BaseResponse<String> sendEmailCode(@RequestParam String email,String type) {
        //对邮箱进行正则表达式判断：

        if(!VerifyRegexUtils.VerifyEmail(email))
        {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "邮箱格式不正确，请检查输入格式");
        }
        try {
            userAccountService.sendEmailCode(email, type);
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统内部出错，邮箱发送失败");
        }
        return ResultUtils.success("验证码发送成功");

    }
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public BaseResponse<String> register(@RequestBody Userinfo user)
    {
        //获取用户名称，邮箱，密码和验证码
        String username = user.getUserName();
        String email = user.getEmail();
        String password = user.getPassword();
        String code = user.getCode();

        // 对用户名进行正则表达式判断：

        if (!VerifyRegexUtils.VerifyName(username)) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "用户名格式不正确，请检查输入格式");
        }
        try
        {
            userAccountService.register( email,username, password, code);
        }catch (BusinessException e)
        {
            return ResultUtils.error(e.getCode(),e.getMessage());
        }


        return ResultUtils.success("注册成功");
    }
    /**
     *用户注销
     */
    @PostMapping("/delete")
    public BaseResponse<String> delete(@RequestBody Userinfo user)
    {
        //获取用户名称，邮箱，密码和验证码

        String email = user.getEmail();
        String password = user.getPassword();
        String code = user.getCode();
        try{
            userAccountService.deleteUserSelf(email,password,code);
        }
        catch (BusinessException e)
        {
            return ResultUtils.error(e.getCode(),e.getMessage());
        }
     return ResultUtils.success("注销成功");

    }
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public BaseResponse<UserinfoVo> login(@RequestBody Userinfo user) {
        //获取用户账户和密码
        String email = user.getEmail();
        String password = user.getPassword();
        // 对用户名进行正则表达式判断：

        if (!VerifyRegexUtils.VerifyEmail(email)) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "邮箱格式不正确，请检查输入格式");
        }
        UserinfoVo userinfoVo;
        try {
            userinfoVo = userAccountService.login(email, password);


        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }

        return ResultUtils.success( userinfoVo);
    }
    /**
     * 重置密码
     */
    @PostMapping("/forgetPassword")
    public BaseResponse<String> forgetPassword(@RequestBody Userinfo user) {
       String account= user.getEmail();
       String code = user.getCode();

       String password = user.getPassword();

       try
       {
           userAccountService.forgetPassword(account, password,code);
       }
       catch (BusinessException e)
       {
           return ResultUtils.error(e.getCode(), e.getMessage());
       }

       return ResultUtils.success("更换成功");
    }

}
