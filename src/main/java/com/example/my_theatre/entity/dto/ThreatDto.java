package com.example.my_theatre.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ThreatDto  implements Serializable {
    /**
     * 座位总数
     */
    public int sumSeat;

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

}
