package com.example.my_theatre.entity.po;


import lombok.AllArgsConstructor;
import lombok.Data;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User  implements Serializable {
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 用户邮箱
     */
    private String userAccount;
    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更改用户信息时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 用户状态
     */
    private int userStatus;


    /**
     * 用户id
     */
    private  int id;


    private static final long serialVersionUID = 1L;
}
