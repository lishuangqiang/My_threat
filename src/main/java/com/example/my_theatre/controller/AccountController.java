package com.example.my_theatre.controller;


import com.example.my_theatre.common.BaseResponse;
import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.entity.dto.Userinfo;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.service.impl.EmailCodeServiceImpl;
import com.example.my_theatre.utils.VerifyRegexUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AccountController {
    @Resource
    EmailCodeServiceImpl emailCodeService;
    /**
     * 发送邮箱验证码
     */
    @PostMapping("/sendEmailCode")
    public BaseResponse<String> sendEmailCode(@RequestParam String email) {
        //对邮箱进行正则表达式判断：
        if(!VerifyRegexUtils.VerifyEmail(email))
        {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "邮箱格式不正确，请检查输入格式");
        }
       emailCodeService.sendEmailCode(email);
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

        emailCodeService.register( email,username, password, code);
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

     emailCodeService.deleteUserSelf(email,password);
     return ResultUtils.success("注销成功");

    }

}
