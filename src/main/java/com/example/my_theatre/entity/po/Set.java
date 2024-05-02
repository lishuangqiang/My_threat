package com.example.my_theatre.entity.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Set{
    /**
     * 座位横坐标
     */
    int xSet;
    /**
     * 座位竖坐标
     */
    int ySet;

    /**
     * 电影名
     */
    String movieName;

    /**
     * 座位状态
     */
    int setState;

    /**
     * 电影开始时间
     */
    String playMovieId;

    private static final long serialVersionUID = 1L;
}
