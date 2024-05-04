package com.example.my_theatre.service.impl;

import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.dto.CommentDto;
import com.example.my_theatre.entity.po.Comment;
import com.example.my_theatre.entity.vo.CommentVo;
import com.example.my_theatre.mapper.CommentMapper;
import com.example.my_theatre.service.UserCommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserCommentServiceImpl implements UserCommentService {
    @Resource
    private CommentMapper commentMapper;

    /**
     * 查询所有评论
     * @return
     */
    @Override
    public List<CommentVo> getAllCommentsByFilmId(int filmId) {
        List<CommentVo> commentList = commentMapper.getAllCommentsByFilmId(filmId);
        List<CommentVo> result = new ArrayList<>();

        // Map 用于存储评论ID和对应的评论对象，方便后续通过评论ID查找评论
        Map<Integer, CommentVo> commentMap = new HashMap<>();

        // 遍历所有评论，构建评论ID到评论对象的映射
        for (CommentVo comment : commentList) {
            commentMap.put(comment.getCommentId(), comment);
        }

        // 遍历评论列表，根据评论的父子关系进行嵌套组织
        for (CommentVo comment : commentList) {
            if (comment.getCommentPid() == 0) {
                // 一级评论，直接添加到结果列表中
                result.add(comment);
            } else {
                // 二级评论，找到对应的一级评论并添加为其子评论
                CommentVo parentComment = commentMap.get(comment.getCommentPid());
                if (parentComment != null) {
                    // 确保找到了父级评论，避免空指针异常
                    List<CommentVo> replies = parentComment.getReplies();
                    if (replies == null) {
                        replies = new ArrayList<>(); // 初始化子评论列表
                        parentComment.setReplies(replies);
                    }
                    replies.add(comment); // 将当前评论添加为父级评论的子评论
                }
            }
        }
        return result;
    }

    /**
     * 用户发表评论
     * @param commentDto
     */
    @Override
    public void insertComment(CommentDto commentDto) {
        commentMapper.insertComment(commentDto);
    }


    /**
     * 用户删除回复
     */
    @Override
    public void deleteComment(Integer commentId) {
        //进行删除（传递用户自己的id，避免恶意删除其他用户的评论）
        commentMapper.deleteByIdandUser(commentId, BaseContext.getCurrentId());
    }


}
