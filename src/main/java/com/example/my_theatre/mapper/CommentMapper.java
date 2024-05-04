package com.example.my_theatre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.my_theatre.entity.dto.CommentDto;
import com.example.my_theatre.entity.po.Admin;
import com.example.my_theatre.entity.po.Comment;
import com.example.my_theatre.entity.vo.CommentVo;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentVo> getAllCommentsByFilmId(int filmId);

    void insertComment(CommentDto commentDto);

    void deleteByIdandUser(Integer commentId, Long currentId);
}
