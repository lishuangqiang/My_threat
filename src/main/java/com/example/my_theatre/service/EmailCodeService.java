package com.example.my_theatre.service;

import com.example.my_theatre.entity.dto.Userinfo;

public interface EmailCodeService {
    void sendEmailCode(String email);

    void register(String Email, String username, String password, String code);


    void deleteUserSelf(String email, String password);
}
