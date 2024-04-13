package com.example.my_theatre.controller.user;


import com.example.my_theatre.annotation.VerifyParam;
import com.example.my_theatre.common.BaseResponse;
import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.entity.dto.UserDto;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.enums.VerifyRegexEnum;
import com.example.my_theatre.entity.vo.UserVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.service.impl.UserAccountServiceImpl;
import com.example.my_theatre.utils.VerifyRegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/user/account")
public class userAccountController {
    @Resource
    UserAccountServiceImpl userAccountService;

    /**
     * 发送验证码  0：注册 1：找回 2： 注销 3：登录
     * @param email
     * @param type
     * @return
     */
    @PostMapping("/sendEmailCode")
   // @VerifyParam(required = true,regexAccount = VerifyRegexEnum.EMAIL)
    public BaseResponse<String> sendEmailCode(@RequestParam String email,String type) {
        //对邮箱进行正则表达式判断：
        log.info("当前用户正在 生成验证码，邮箱："+ email + ",验证码类型："+type);
        if(!VerifyRegexUtils.VerifyEmail(email))
        {
            return ResultUtils.error(ErrorCode.ACCOUNT_ERROR, "邮箱格式不正确，请检查输入格式");
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
    @VerifyParam(required = true,regexAccount = VerifyRegexEnum.EMAIL,regexPassword = VerifyRegexEnum.PASSWORD,regexName = VerifyRegexEnum.USER_NAME)
    public BaseResponse<String> register(@RequestBody UserDto user)
    {
        log.info("当前用户正在注册， 账户名：{}"+user.getEmail());
        //获取用户名称，邮箱，密码和验证码
        String username = user.getUserName();
        String email = user.getEmail();

        String password = user.getPassword();
        String code = user.getCode();
        try
        {
            userAccountService.register( email,username, password, code);
        }
        catch (BusinessException e)
        {
            return ResultUtils.error(e.getCode(),e.getMessage());
        }


        return ResultUtils.success("注册成功");
    }
    /**
     *用户注销
     */
    @PostMapping("/delete")
    @VerifyParam(required = true, regexAccount = VerifyRegexEnum.EMAIL,regexName = VerifyRegexEnum.USER_NAME,regexPassword = VerifyRegexEnum.PASSWORD)
    public BaseResponse<String> delete(@RequestBody UserDto user)
    {
        log.info("当前用户正在注销， 账户名：{}"+user.getEmail());
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
    @VerifyParam(required = true, regexAccount = VerifyRegexEnum.EMAIL,regexPassword = VerifyRegexEnum.PASSWORD)
    public BaseResponse<UserVo> login(@RequestBody UserDto user) {
        log.info("当前用户通过密码正在登录， 账户名：{}"+user.getEmail());
        //获取用户账户和密码
        String email = user.getEmail();
        String password = user.getPassword();
        UserVo userinfoVo;

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
    @VerifyParam(required = true, regexAccount = VerifyRegexEnum.EMAIL)
    public BaseResponse<String> forgetPassword(@RequestBody UserDto user) {
        log.info("当前用户正在更换密码， 账户名：{}"+user.getEmail());
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
    /**
     * 通过验证码登录
     */
    @PostMapping("/loginByCode")
    @VerifyParam(required = true, regexAccount = VerifyRegexEnum.EMAIL)
    public BaseResponse<UserVo> loginByCode(@RequestBody UserDto user) {
        log.info("当前用户正在通过验证码登录："+ user.getEmail());
        String userAccount= user.getEmail();
        String code = user.getCode();
        UserVo userinfoVo;
        try {
            userinfoVo = userAccountService.loginByCode(userAccount,code);
        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success(userinfoVo);

    }

}
