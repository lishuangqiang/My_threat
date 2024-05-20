package com.example.my_theatre.service.impl;

import com.example.my_theatre.config.RedissonConfig;
import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.config.RedisConfig;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.po.Applyorder;
import com.example.my_theatre.entity.po.Threat;
import com.example.my_theatre.entity.vo.OrderVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.mapper.*;
import com.example.my_theatre.service.AdminOrderService;
import com.example.my_theatre.utils.AliOssUtil;
import com.example.my_theatre.utils.OrderIdUtil;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.support.DefaultTransactionDefinition;


import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    @Resource
    private FilmMapper filmMapper;
    @Autowired
    private PlatformTransactionManager transactionManager;


    @Resource
    private RedissonClient redissonClient;

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
     * 管理端下单（存在线程安全问题）
     * @param playmovieId
     * @param x_set
     * @param y_set
     * @return
     * @throws IOException
     * @throws WriterException
     * @throws BusinessException
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    @Override
    public String createOrderByAdmin(int playmovieId, int x_set, int y_set) throws IOException, WriterException, BusinessException {
        // 查询当前场次的movie
        Threat threat = threatMapper.selectBymovieId(playmovieId);
        if (threat == null) {
            throw new BusinessException(ErrorCode.THREAT_IS_NULL);
        }

        RLock lock = redissonClient.getLock("createOrderLock");
        try {
            // 尝试加锁，最多等待3秒
            boolean locked = lock.tryLock(3, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
            if (threat.getSealSit() >= threat.getSumSeat()) { // 座位已满时回滚事务

                throw new BusinessException(ErrorCode.SET_IS_FULL);
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

            // 开启数据库事务
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            TransactionStatus status = transactionManager.getTransaction(def);

            try {
                // 向数据库插入订单信息
                orderMapper.insertOrder(orderId, playmovieId, movieName, orderUser, orderStatus, QRcodeUrl);

                // 订单创建成功之后扣减库存
                threatMapper.updatesealsit(playmovieId);

                // 更新电影的票房纪录
                filmMapper.updateTicket();

                // 将座位写入列表中
                setMapper.insertSet(x_set, y_set, movieName, playmovieId);

                // 提交事务
                transactionManager.commit(status);

            } catch (Exception e) {
                // 回滚事务
                transactionManager.rollback(status);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            } finally {
                // 释放锁
                lock.unlock();
            }

            // 返回订单二维码
            return QRcodeUrl;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        } finally {
            // 确保在发生异常时也能释放锁
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
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
