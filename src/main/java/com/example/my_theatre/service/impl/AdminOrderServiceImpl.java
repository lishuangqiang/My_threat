package com.example.my_theatre.service.impl;

import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.vo.OrderVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.mapper.OrderMapper;
import com.example.my_theatre.service.AdminOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AdminOrderServiceImpl implements AdminOrderService {
    @Resource
    public OrderMapper orderMapper;


    /**
     * 分页查询所有订单
     * @param page
     * @param size
     * @return
     */
    @Override
    public List<OrderVo> getorderBypage(int page, int size) {
        int start = (page - 1) * size;
        Map<String, Integer> params = new HashMap<>();
        params.put("start", start);
        params.put("size", size);
        return orderMapper.getallorderBypage(params);
    }

    /**
     * 管理员端取消订单
     * @param orderId
     */
    public void cancleOrder(String orderId) throws BusinessException {
        //先检查订单是否非空
        if(orderId.equals(null))
        {
            throw  new BusinessException(ErrorCode.ORDER_IS_NULL);
        }
        if(orderMapper.cancleOrder(orderId)==Boolean.FALSE)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }



    }


}
