package com.example.my_theatre.controller.common;

import com.example.my_theatre.common.BaseResponse;
import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.entity.vo.CommentVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.service.impl.UserCommentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private UserCommentServiceImpl userCommentService;

    /**
     *查询当前电影所有评论
     */
    @GetMapping("/getAllcomments")
    public BaseResponse<List<CommentVo>> cancelOrderId(@RequestParam Integer FilmId)
    {
        log.info("查询当前电影所有的评论");
        List<CommentVo> result;
        try {

            result = userCommentService.getAllCommentsByFilmId(FilmId);
        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success(result);
    }
}
