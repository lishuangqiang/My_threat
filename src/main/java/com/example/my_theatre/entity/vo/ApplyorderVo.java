package com.example.my_theatre.entity.vo;

import lombok.Data;

/**
 * 退单表
 */
@Data
public class ApplyorderVo {
    /**
     * 订单id
     */
    String orderId;

    /**
     * 申请账户
     */
    String applyAccount;

    /**
     * 审批结果(0:未同意，1：已同意，2：未审批)
     */
    String isAgree;

    /**
     * 申请原因
     */
    String applyReason;

    private static final long serialVersionUID = 1L;
}