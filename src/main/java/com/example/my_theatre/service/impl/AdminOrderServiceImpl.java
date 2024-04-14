package com.example.my_theatre.service.impl;

import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.po.Applyorder;
import com.example.my_theatre.entity.po.Threat;
import com.example.my_theatre.entity.vo.OrderVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.mapper.ApplyorderMapper;
import com.example.my_theatre.mapper.OrderMapper;
import com.example.my_theatre.mapper.SetMapper;
import com.example.my_theatre.mapper.ThreatMapper;
import com.example.my_theatre.service.AdminOrderService;
import com.example.my_theatre.utils.AliOssUtil;
import com.example.my_theatre.utils.OrderIdUtil;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AdminOrderServiceImpl implements AdminOrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private ThreatMapper threatMapper;
    @Resource
    private AliOssUtil aliOssUtil;
    @Resource
    private SetMapper setMapper;
    @Resource
    private ApplyorderMapper applyorderMapper;


    /**
     * 分页查询所有订单
     *
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
     *
     * @param orderId
     */
    public void cancleOrder(String orderId) throws BusinessException {
        //先检查订单是否非空
        if (orderId.equals(null)) {
            throw new BusinessException(ErrorCode.ORDER_IS_NULL);
        }
        if (orderMapper.cancleOrder(orderId) == Boolean.FALSE) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    /**
     * 管理员端下单
     */
    //todo
    //添加锁防止并发，不要直接加重量级锁
    //表设计有问题，后续需要更改。
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String createOrderByAdmin(int playmovieId, int x_set, int y_set) throws IOException, WriterException, BusinessException {
        // 查询当前场次的movie
        Threat threat = threatMapper.selectBymovieId(playmovieId);
        if (threat == null) {
            throw new BusinessException(ErrorCode.THREAT_IS_NULL);
        }


        if (threat.getSealSit() == threat.getSumSeat()) {
            log.info("座位已满");
            throw new BusinessException(ErrorCode.SET_IS_FULL);
        }
        // 检查当前座位是否有人
        if (setMapper.select(x_set, y_set) == Boolean.TRUE) {
            throw new BusinessException(ErrorCode.SET_IS_USED);
        }

        // 获取当前场次电影的信息
        String movieName = threat.getMovieName();

        // 生成订单id
        String orderId = OrderIdUtil.getUUID();

        // 订单用户（这里记录管理员的id）
        String orderUser = String.valueOf(BaseContext.getCurrentId());

        // 订单状态
        int orderStatus = 1;

        // 将基本信息存储到HashMap中
        HashMap<String, Object> orderInfo = new HashMap<>();
        orderInfo.put("movieName", movieName);
        orderInfo.put("orderId", orderId);
        orderInfo.put("orderUser", orderUser);
        orderInfo.put("orderStatus", orderStatus);

        // 生成二维码并且上传至阿里云oss
        String QRcodeUrl = aliOssUtil.CreateQRandUpload(orderInfo);

        // 向数据库插入订单信息
        if (orderMapper.insertOrder(orderId, playmovieId, movieName, orderUser, orderStatus, QRcodeUrl) == Boolean.FALSE) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        // 订单创建成功之后扣减库存
        threatMapper.updatesealsit(playmovieId);

        // 将座位写入列表中
        setMapper.insertSet(x_set, y_set, movieName, playmovieId);

        // 返回订单二维码
        return QRcodeUrl;
    }

    /**
     * 分页查询退单表
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public List<Applyorder> findApprovalOrder(int page, int size) {
        int start = (page - 1) * size;
        Map<String, Integer> params = new HashMap<>();
        params.put("start", start);
        params.put("size", size);
        return applyorderMapper.selectApprovalOrder(params);
    }

    /**
     * 管理员审批退单请求
     * @param orderId
     * @param status
     * @throws BusinessException
     */
    @Override
    public void ApprovalOrder(String orderId, int status) throws BusinessException {
        if (orderId == null) {
            throw new BusinessException(ErrorCode.ORDER_IS_NULL);
        }
       if(applyorderMapper.updateApproval(orderId, status) == Boolean.FALSE)
       {
           throw new BusinessException(ErrorCode.SYSTEM_ERROR);
       }
    }


}
