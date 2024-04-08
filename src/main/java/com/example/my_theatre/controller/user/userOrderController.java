package com.example.my_theatre.controller.user;

import com.example.my_theatre.common.BaseResponse;
import com.example.my_theatre.service.UserOrderService;
import com.example.my_theatre.service.impl.UserOrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/user/order")
public class userOrderController {
    @Resource
    private UserOrderServiceImpl userOrderServiceImpl;

}
