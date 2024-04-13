package com.example.my_theatre.controller.admin;

import com.example.my_theatre.common.BaseResponse;
import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.po.Applyorder;
import com.example.my_theatre.entity.vo.FilmVo;
import com.example.my_theatre.entity.vo.OrderVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.mapper.ApplyorderMapper;
import com.example.my_theatre.service.impl.AdminOrderServiceImpl;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.encoder.QRCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/order")
public class adminOrderController {
    @Resource
    public AdminOrderServiceImpl adminOrderService;
    @Resource
    private ApplyorderMapper applyorderMapper;

    /**
     * 查询所有订单
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/allOrder")
    public BaseResponse<List<OrderVo>> allorder(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询数据库所有订单....." + "当前管理员id为：" + BaseContext.getCurrentId());
        List<OrderVo> films = adminOrderService.getorderBypage(page, size);
        return ResultUtils.success(films);
    }

    /**
     * 设置订单状态（ 管理员端禁用本次订单）
     */
    @GetMapping("/cancleOrder")
    public BaseResponse<String> cancleOrder(@RequestParam String orderId) {
        log.info("管理员端取消该订单状态 ....." + orderId + "当前管理员id为：" + BaseContext.getCurrentId());
        try {
            adminOrderService.cancleOrder(orderId);
        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success("设置订单状态成功");
    }

    /**
     * 管理员端下单
     *
     * @param playmovieId
     * @return todo
     * 存在并发bug
     * 这里的思路为：返回一个包含订单信息的二维码。后续可以抽象这个接口为通用接口
     */
    @GetMapping("/createOrderByAdmin")
    public BaseResponse<String> createOrderByAdmin(@RequestParam int playmovieId,
                                                   @RequestParam int x_set,
                                                   @RequestParam int y_set) {
        log.info("管理员端下单，" + "当前管理员id为：" + BaseContext.getCurrentId());
        String QRcode;
        try {
            QRcode = adminOrderService.createOrderByAdmin(playmovieId, x_set, y_set);
        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (IOException e) {
            //读文件上传至oss发生异常
            throw new RuntimeException(e);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

        return ResultUtils.success(QRcode);
    }

    /**
     * 申批用户退单请求
     * 0:不同意
     * //todo(完成退单请求审批
     */
    @GetMapping("/approvalOrder")
    public BaseResponse<String> ApprovalOrder(@RequestParam String orderId, @RequestParam int status) {
        log.info("管理员审批退单 ....." + orderId + "当前管理员id为：" + BaseContext.getCurrentId());
        try {
            adminOrderService.ApprovalOrder(orderId, status);
        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success("审批该退单成功");
    }


    /**
     * 分页查询所有退单请求
     * todo(后续引入websocket来提醒管理员有人退单以及提醒用户退单成功)
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/findapprovalOrder")
    public BaseResponse<List<Applyorder>> findApprovalOrder(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("管理员端查看退单申请 ....." + "当前管理员id为：" + BaseContext.getCurrentId());
        List<Applyorder> list;

        list = adminOrderService.findApprovalOrder(page, size);

        return ResultUtils.success(list);
    }


}
