package com.example.my_theatre.entity.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class FilmDto implements Serializable {
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
    public int movieTime;

    /**
     * 剧照
     */
    public MultipartFile moviePicture;

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
}
