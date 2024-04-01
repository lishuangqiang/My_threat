package com.example.my_theatre.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin implements Serializable {
    /**
     * 管理员姓名
     */
    String adminName;

    /**
     * 管理员账户
     */
    String adminAccount;

    /**
     * 管理员密码
     */
    String adminPassword;

    private static final long serialVersionUID = 1L;


}
