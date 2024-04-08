package com.example.my_theatre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.my_theatre.entity.po.Admin;
import com.example.my_theatre.entity.po.Order;
import com.example.my_theatre.entity.vo.OrderVo;

import java.util.List;
import java.util.Map;

public interface OrderMapper extends BaseMapper<Order> {

    List<OrderVo> getallorderBypage(Map<String, Integer> params);


    Boolean cancleOrder(String orderId);
}
