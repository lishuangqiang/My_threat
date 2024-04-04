package com.example.my_theatre.entity.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FilmDto {
    /**
     * 电影名称
     */
    public String movieName;


    /**
     * 主演
     */
    public String leadingActor;

    /**
     * 电影时长
     */
    public int moiveTime;

    /**
     * 剧照
     */
    public MultipartFile moviePicture;

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
