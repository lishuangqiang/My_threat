package com.example.my_theatre.controller;


import com.example.my_theatre.service.impl.EmailCodeServiceImpl;


import org.springframework.web.bind.annotation.PostMapping;
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
    public void  sendEmailCode(@RequestParam String email) {
    emailCodeService.sendEmailCode(email);

    }


}
