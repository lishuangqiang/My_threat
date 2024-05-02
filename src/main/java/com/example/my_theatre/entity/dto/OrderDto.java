package com.example.my_theatre.entity.dto;

import java.io.Serializable;

public class OrderDto implements Serializable {

    /**
     * 电影id
     */
    public int movieId;

    /**
     * 电影名称
     */
    public String movieName;

    /**
     * 下单用户
     */
    public String orderUser;

    /**
     * 订单支付状态(0:未支付，1：已支付)
     */
    public int orderPayStatue;

    /**
     * 订单使用状态（0：未使用，1：已使用，2：被禁止）
     */
    public int orderUserStatue;

    private static final long serialVersionUID = 1L;
}