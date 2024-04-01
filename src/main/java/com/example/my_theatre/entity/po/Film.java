package com.example.my_theatre.entity.po;

import lombok.Data;

@Data
public class Film {
    /**
     * 电影id
     */
    public int movieId;

    /**
     * 电影名称
     */
    public String movieName;


    /**
     * 主演
     */
    public String leadingActor;

    /**
     * 剧照
     */
    public String moviePicture;

    /**
     * 电影时长
     */
    public int moiveTime;

    /**
     * 总售票数量
     */
    public int numberTicket;

    /**
     * 电影国家
     */
    public String moiveCountry;

    /**
     * 电影类型
     */
    public String moiveType;

    /**
     * 电影上映年份
     */
    public String movieYear;
}
