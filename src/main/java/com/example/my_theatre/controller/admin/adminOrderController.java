package com.example.my_theatre.controller.admin;

import com.example.my_theatre.common.BaseResponse;
import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.entity.vo.FilmVo;
import com.example.my_theatre.entity.vo.OrderVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.service.impl.AdminOrderServiceImpl;
import com.google.zxing.qrcode.encoder.QRCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/order")
public class adminOrderController {
    @Resource
    public AdminOrderServiceImpl adminOrderService;

    /**
     * 查询所有订单
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/allOrder")
    public BaseResponse<List<OrderVo>> allorder(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询数据库所有订单.....");
        List<OrderVo> films = adminOrderService.getorderBypage(page, size);
        return ResultUtils.success(films);
    }

    /**
     * 设置订单状态（ 管理员端禁用本次订单）
     * */
    @GetMapping("/cancleOrder")
    public BaseResponse<String>cancleOrder(@RequestParam String orderId) {
        log.info("管理员端取消该订单状态 ....."+ orderId);
        try {
            adminOrderService.cancleOrder(orderId);
        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success("设置订单状态成功");
    }

    /**
     * 管理员端下单
     * @param Name
     * @return
     * todo
     * 这里的思路为：返回一个包含订单信息的二维码。后续可以抽象这个接口为通用接口
     */
//    @GetMapping("/createOrderByAdmin")
//    public BaseResponse<QRCode> createOrderByAdmin(String Name) {
//        log.info("管理员端下单");
//        QRCode qrCode;
//        try {
//            qrCode = adminOrderService.createOrderByAdmin(Name);
//        } catch (BusinessException e) {
//            return ResultUtils.error(e.getCode(), e.getMessage());
//        }
//        return ResultUtils.success(qrCode);
//    }





}
