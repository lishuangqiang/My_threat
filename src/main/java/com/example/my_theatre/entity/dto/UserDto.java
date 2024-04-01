package com.example.my_theatre.entity.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求实体类

 */
@Data
public class UserDto implements Serializable {


    /**
     * 用户账号
     */
    private String email;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户验证码
     */
    private String code;



    private static final long serialVersionUID = 1L;
}