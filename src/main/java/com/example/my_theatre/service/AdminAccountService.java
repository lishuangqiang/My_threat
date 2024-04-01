package com.example.my_theatre.service;

import com.example.my_theatre.entity.dto.AdminDto;
import com.example.my_theatre.entity.po.Admin;
import com.example.my_theatre.entity.vo.AdminVo;

import java.util.List;

public interface AdminAccountService {
    AdminVo login(String account, String password);

    void addAdmin(AdminDto adminDto);

    void delAdmin(AdminDto adminDto);

    List<Admin> findall();
}
