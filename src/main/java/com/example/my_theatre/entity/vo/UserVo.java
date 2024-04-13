package com.example.my_theatre.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
@AllArgsConstructor
public class UserVo {

    /**
     * 用户账号
     */
    private String email;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户携带token
     */
    private String token;

    /**
     * 用户id
     */
    private  int id;

}
