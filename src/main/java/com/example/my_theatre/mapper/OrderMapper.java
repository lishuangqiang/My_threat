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


    Boolean insertOrder(String orderId, int playmovieId, String movieName,
                     String orderUser, int orderStatus, String qRcodeUrl);


    List<OrderVo> selectOrderByPage(Map<String, Integer> params, Long currentId);


    List<OrderVo> getuserorderBypage(Map<String, Integer> params, String userAccount);

    Order selectByorderId(String orderId);

    List<OrderVo> selectOrderByPageandName(Map<String, Integer> params, Long currentId, String filename);
}
