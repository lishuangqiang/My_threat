package com.example.my_theatre.service.impl;

import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.constants.JwtClaimsConstant;
import com.example.my_theatre.entity.dto.AdminDto;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.po.Admin;
import com.example.my_theatre.entity.vo.AdminVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.mapper.AdminMapper;
import com.example.my_theatre.properties.JwtProperties;
import com.example.my_theatre.service.AdminAccountService;
import com.example.my_theatre.utils.JwtUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminAccountServiceImpl implements AdminAccountService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private JwtProperties jwtProperties;


    /**
     * 管理员登录
     *
     * @param account
     * @param password
     * @return
     */
    @Override
    public AdminVo login(String account, String password) throws BusinessException {
        //对密码字符串进行MD5加密
        //todo(密码加密放到前端去做)
        String AdminintoPassword = password;
        //根据用户邮箱搜索用户
        Admin admin = adminMapper.selectByAccount(account);
        //进行密码比对
        if (!admin.getAdminPassword().equals(AdminintoPassword)) {
            throw new BusinessException(ErrorCode.PASSWPRD_ERROR, "用户密码不正确，请重新输入或获取");
        }

        //写入token
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.ADMIN_ACCOUNT, admin.getAdminAccount());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        // 封装返回给前端的用户实体
        AdminVo adminVo = new AdminVo();
        adminVo.setAdminAccount(admin.getAdminAccount());
        adminVo.setAdminName(admin.getAdminName());
        adminVo.setToken(token);
        return adminVo;
    }


    /**
     * 添加管理员
     *
     * @param adminDto
     */
    @Override
    public void addAdmin(AdminDto adminDto) {
        //检查是否为管理员操作
        String nowAccount = String.valueOf(BaseContext.getCurrentId());
        if (adminMapper.selectByAccount(nowAccount )==null) {
            throw new BusinessException(ErrorCode.NO_MAIN_OPERATION, "操作账号不正确");

        }

        String adminPassword = adminDto.getAdminPassword();
        String adminAccount = adminDto.getAdminAccount();
        String adminName = adminDto.getAdminName();
        //插入
        try
        {
            adminMapper.addAdmin(adminAccount, adminName, adminPassword);
        }
        catch(Exception e)
        {
            throw new BusinessException(ErrorCode.SERVER_ERROR, "添加管理员失败");
        }
    }

    /**
     * 删除管理员
     * @param adminDto
     */
    @Override
    public void delAdmin(AdminDto adminDto) {
        //检查是否为管理员操作
        String nowAccount = String.valueOf(BaseContext.getCurrentId());
        if (adminMapper.selectByAccount(nowAccount )==null) {
            throw new BusinessException(ErrorCode.NO_MAIN_OPERATION, "操作账号不正确");

        }

        String adminAccount = adminDto.getAdminAccount();
        try
        {
            adminMapper.delAdmin(adminAccount);
        }
        catch (Exception e)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除管理员失败");
        }
    }

    /**
     * 查询所有管理员
     * @return
     */
    @Override
    public List<Admin> findall() {
        //检查是否为管理员操作
        String nowAccount = String.valueOf(BaseContext.getCurrentId());
        if (adminMapper.selectByAccount(nowAccount) == null) {
            throw new BusinessException(ErrorCode.NO_MAIN_OPERATION, "操作账号不正确");

        }

        List<Admin> listadmin;
        try {
            listadmin = adminMapper.findall();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取所有管理员失败");
        }
        return listadmin;
    }

}
