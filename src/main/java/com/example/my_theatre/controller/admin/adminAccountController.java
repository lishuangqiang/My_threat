package com.example.my_theatre.controller.admin;


import com.example.my_theatre.common.BaseResponse;
import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.dto.AdminDto;
import com.example.my_theatre.entity.po.Admin;
import com.example.my_theatre.entity.vo.AdminVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.service.impl.AdminAccountServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/account")
public class adminAccountController {
    @Resource
    AdminAccountServiceImpl adminAccountService;

    /**
     * 管理员登录
     *
     * @param adminDto
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<AdminVo> login(@RequestBody AdminDto adminDto) {
        log.info("当前管理员正在尝试登录，管理员账号为:" + BaseContext.getCurrentId());

        String account = adminDto.getAdminAccount();
        String password = adminDto.getAdminPassword();
        AdminVo adminVo;
        try {

            adminVo = adminAccountService.login(account, password);

        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }

        return ResultUtils.success(adminVo);
    }

    /**
     * 添加管理员
     *
     * @param adminDto
     * @return
     */
    @PostMapping("/addAdmin")
    public BaseResponse<String> addAdmin(@RequestBody AdminDto adminDto) {

        log.info("当前管理员正在尝试添加管理员，管理员账号为:" + BaseContext.getCurrentId());
        try {
            adminAccountService.addAdmin(adminDto);
        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success("插入成功");
    }

    @PostMapping("/deleteAdmin")
    public BaseResponse<String> delAdmin(@RequestBody AdminDto adminDto) {

        log.info("当前管理员正在尝试删除管理员，管理员账号为：" + BaseContext.getCurrentId());
        try {
            adminAccountService.delAdmin(adminDto);
        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success("删除成功");
    }

    /**
     * 查询所有管理员
     */
    @GetMapping("/findAll")
    public BaseResponse<List<Admin>> findAll() {
        log.info("查询所有管理员");
        List<Admin> listadmins = null;
        try {
            listadmins = adminAccountService.findall();
        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success(listadmins);
    }

}
