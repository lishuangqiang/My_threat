package com.example.my_theatre.controller.user;

import com.example.my_theatre.common.BaseResponse;
import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.po.Order;
import com.example.my_theatre.entity.vo.OrderVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.service.UserOrderService;
import com.example.my_theatre.service.impl.UserOrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user/order")
public class userOrderController {
    @Resource
    private UserOrderServiceImpl userOrderService;


    /**
     * 用户根据订单id取消订单
     * @param orderId
     * @return
     */
    @DeleteMapping("/cancelOrderId")
    public BaseResponse<String> cancelOrderId(String orderId)
    {
        log.info("当前用户正在尝试取消订单，用户账号为：" + BaseContext.getCurrentId());
        try {
            userOrderService.cancelOrderId(orderId);
        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success("正在尝试取消订单");
    }

    /**
     * 用户查询自己的所有订单
     */
    @GetMapping("/allOrder")
    public BaseResponse<List<OrderVo>> getAllOrder(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("当前用户正在尝试查看自己的所有订单");
        List<OrderVo> list;
        try {
            list = userOrderService.getAllOrder(BaseContext.getCurrentId(), page, size);
        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success(list);
    }


}
