package com.example.my_theatre.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 评论类
 */
@Data
public class CommentDto {
    /**
     * 评论id
     */
    public int commentId;

    /**
     * 用户id
     */
    public int userId;

    /**
     * 用户名称
     */
    public String userName;

    /**
     * 评论所属顶父级id
     */
    public int commentPid;

    /**
     * 评论电影
     */
    public int targetFilmId;

    /**
     * 评论内容
     */
    public String content;

    /**
     * 回复用户
     */
    public String toUserId;

    /**
     * 发表时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date creatTime;

    /**
     * 点赞数
     */
    public int likesCount;

    /**
     * 点踩数
     */
    public int dislikeCount;


    private static final long serialVersionUID = 1L;
}
