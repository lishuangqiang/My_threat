package com.example.my_theatre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.my_theatre.entity.po.Admin;

import java.util.List;

public interface AdminMapper extends BaseMapper<Admin> {
    Admin selectByAccount(String account);

    void addAdmin(String adminAccount, String adminName, String adminPassword);

    void delAdmin(String adminAccount);

    List<Admin> findall();

    Admin getByaccount(String adminId);
}
