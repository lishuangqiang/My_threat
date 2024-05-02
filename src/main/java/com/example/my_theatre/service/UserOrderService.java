package com.example.my_theatre.service;

import com.example.my_theatre.entity.vo.OrderVo;
import com.example.my_theatre.exception.BusinessException;

import java.util.List;

public interface UserOrderService {

    List<OrderVo> getAllOrder(Long currentId, int page, int size);

    public void cancelOrderId(String orderId) throws BusinessException;

    List<OrderVo> getAllOrderByname(Long currentId, int page, int size, String filename);
}
