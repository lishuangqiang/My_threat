package com.example.my_theatre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.my_theatre.entity.po.User;


public interface UserMapper extends BaseMapper<User> {


    User findUserByemail(String email);

    void register(User user);

    void deleteUserByEmail(String email);

    void updatePasswordByemail(String email, String password);
}




