package com.example.my_theatre.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data

@NoArgsConstructor
@AllArgsConstructor
public class AdminVo implements Serializable {
    /**
     * 管理员名称
     */
    String adminName;

    /**
     * 管理员Id
     */
    String adminAccount;


    /**
     * 管理员携带token
     */
    private String token;

    private static final long serialVersionUID = 1L;
}
