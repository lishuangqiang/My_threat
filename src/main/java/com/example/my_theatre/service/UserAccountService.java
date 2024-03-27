package com.example.my_theatre.service;

import com.example.my_theatre.entity.po.User;
import com.example.my_theatre.entity.vo.UserinfoVo;
import com.example.my_theatre.exception.BusinessException;
import jakarta.mail.MessagingException;

public interface UserAccountService {
    void sendEmailCode(String email,String type) throws MessagingException;

    void register(String Email, String username, String password, String code)throws BusinessException;


    void deleteUserSelf(String email, String password,String code)throws BusinessException;

    UserinfoVo login(String email, String password)throws BusinessException;

    void forgetPassword(String account, String password,String code) throws BusinessException;
}
