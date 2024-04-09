package com.example.my_theatre.service;

import com.example.my_theatre.entity.vo.OrderVo;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.util.List;

public interface AdminOrderService {

    List<OrderVo> getorderBypage(int page, int size);

    void cancleOrder(String  orderId);

    String  createOrderByAdmin(int playmovieId,int x_set,int y_set) throws IOException, WriterException;
}
