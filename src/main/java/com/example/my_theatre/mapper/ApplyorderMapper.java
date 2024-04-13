package com.example.my_theatre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.po.Applyorder;
import com.example.my_theatre.entity.po.User;

import java.util.List;
import java.util.Map;

public interface ApplyorderMapper extends BaseMapper<Applyorder> {

    Boolean insertNewOrder(String orderId, Long userId);

    List<Applyorder> selectApprovalOrder(Map<String, Integer> params);

    Boolean updateApproval(String orderId, int status);
}