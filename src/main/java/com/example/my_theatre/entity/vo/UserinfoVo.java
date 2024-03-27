package com.example.my_theatre.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
@AllArgsConstructor
public class UserinfoVo {

    private String email;

    private String userName;

    private String token;

}
