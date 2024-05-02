package com.example.my_theatre.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ThreatVo {
    /**
     * 上映电影id
     */
    public int playingMovieId;


    /**
     * 电影开始时间
     */
    public LocalDateTime startTime;

    /**
     * 电影结束时间
     */
    public LocalDateTime endTime;

    /**
     * 电影名称
     */
    public String movieName;

    /**
     * 电影名称
     */
    public String moviePicture;

    private static final long serialVersionUID = 1L;
}
