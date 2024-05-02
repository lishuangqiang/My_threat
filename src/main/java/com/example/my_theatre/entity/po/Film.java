package com.example.my_theatre.entity.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class Film implements Serializable {
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
    public int movieTime;

    /**
     * 总售票数量
     */
    public int numberTicket;

    /**
     * 电影国家
     */
    public String movieCountry;

    /**
     * 电影类型
     */
    public String movieType;

    /**
     * 电影上映年份
     */
    public String movieYear;

    private static final long serialVersionUID = 1L;
}
