package com.example.my_theatre.service;

import com.example.my_theatre.entity.vo.OrderVo;

import java.util.List;

public interface AdminOrderService {

    List<OrderVo> getorderBypage(int page, int size);

    void cancleOrder(String  orderId);
}
