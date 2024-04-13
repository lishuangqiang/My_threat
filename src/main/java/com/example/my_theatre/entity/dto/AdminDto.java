package com.example.my_theatre.entity.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class AdminDto implements Serializable {
    /**
     * 管理员id
     */
    int adminId;
    /**
     * 用户名称
     */
    String adminName;

    /**
     * 管理员Id
     */
    String adminAccount;

    /**
     * 管理员密码
     */
    String adminPassword;

    private static final long serialVersionUID = 1L;
}
