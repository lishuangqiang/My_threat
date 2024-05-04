package com.example.my_theatre.service;

import com.example.my_theatre.entity.dto.CommentDto;
import com.example.my_theatre.entity.vo.CommentVo;

import java.util.List;

public interface UserCommentService {
    List<CommentVo> getAllCommentsByFilmId(int filmId);

    void insertComment(CommentDto commentDto);

    void deleteComment(Integer commentId);
}
