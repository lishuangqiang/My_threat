package com.example.my_theatre.service;

import com.example.my_theatre.entity.vo.OrderVo;

import java.util.List;

public interface UserOrderService {

    List<OrderVo> getAllOrder(Long currentId, int page, int size);
}
