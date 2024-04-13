package com.example.my_theatre.service.impl;

import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.po.Order;
import com.example.my_theatre.entity.vo.OrderVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.mapper.ApplyorderMapper;
import com.example.my_theatre.mapper.OrderMapper;
import com.example.my_theatre.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserOrderServiceImpl implements UserOrderService {
    @Autowired
    private   OrderMapper orderMapper;
    @Autowired
    private ApplyorderMapper applyorderMapper;

    /**
     * 删除订单（逻辑删除，避免内存页合并）
     * @param orderId
     */
    public void cancelOrderId(String orderId) throws BusinessException {
        //根据订单id查询订单：
        Order order= orderMapper.selectByorderId(orderId);
        //检查订单是否存在：
        if(order==null)
        {
            throw new BusinessException(ErrorCode.ORDER_NOT_EXIST);
        }
        //检查订单支付状态（如果已经支付那就发送申请取消订单，如果没有支付就直接取消订单）
        if(order.getOrderPayStatue()== 0)
        {
            if(orderMapper.cancleOrder(orderId)==Boolean.FALSE)
            {
                throw  new BusinessException(ErrorCode.CANCEL_ORDER_FAIL);
            }
        }
        else
        {
            //获取当前用户id
            Long currentId = BaseContext.getCurrentId();
            //写入申请表，申请取消订单
            if(applyorderMapper.insertNewOrder(orderId,currentId)==Boolean.FALSE)
            {
                throw  new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }

    /**
     * 查询当前用户的所有订单
     * @param currentId
     */
    @Override
    public List<OrderVo> getAllOrder(Long currentId, int page, int size) {
        int start = (page - 1) * size;
        Map<String, Integer> params = new HashMap<>();
        params.put("start", start);
        params.put("size", size);
        return orderMapper.selectOrderByPage(params,currentId);
    }
}
