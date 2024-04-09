package com.example.my_theatre.service.impl;

import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.po.Threat;
import com.example.my_theatre.entity.vo.OrderVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.mapper.OrderMapper;
import com.example.my_theatre.mapper.SetMapper;
import com.example.my_theatre.mapper.ThreatMapper;
import com.example.my_theatre.service.AdminOrderService;
import com.example.my_theatre.utils.AliOssUtil;
import com.example.my_theatre.utils.OrderIdUtil;
import com.example.my_theatre.utils.RedisUtil;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class AdminOrderServiceImpl implements AdminOrderService {
    @Resource
    public OrderMapper orderMapper;
    @Resource
    public ThreatMapper threatMapper;
    @Resource
    public AliOssUtil aliOssUtil;
    @Resource
    public SetMapper setMapper;

    private static final Object lock = new Object();

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

        // 检查当前场次是否满员
        if (threat.getSealSit() == threat.getSumSeat()) {
            log.info("座位已满");
            throw new BusinessException(ErrorCode.SET_IS_FULL);
        }

        // 订单创建成功之后扣减库存
        synchronized (lock) {
//            // 检查当前座位是否有人
//            if (setMapper.select(x_set, y_set) == Boolean.TRUE) {
//                throw new BusinessException(ErrorCode.SET_IS_USED);
//            }

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
    }



}
