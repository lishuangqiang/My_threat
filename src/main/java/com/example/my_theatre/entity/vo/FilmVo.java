package com.example.my_theatre.entity.vo;

import lombok.Data;

@Data
public class FilmVo {
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

    /**
     * 电影评分人数
     */
    public int  ratingNumber;

    /**
     * 电影总分
     */
    public int ratingSum;


    private static final long serialVersionUID = 1L;
}
