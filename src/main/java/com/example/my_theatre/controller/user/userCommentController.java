package com.example.my_theatre.controller.user;

import com.example.my_theatre.common.BaseResponse;
import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.dto.CommentDto;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.vo.CommentVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.service.impl.UserCommentServiceImpl;
import com.example.my_theatre.utils.VerifyRegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户评论相关接口
 */
@RestController
@Slf4j
@RequestMapping("/user/comment")
public class userCommentController {
    @Resource
    private UserCommentServiceImpl userCommentService;

    /**
     * 用户评论
     * @param commentDto
     * @return
     */
    @PostMapping("/publisComment")
    public BaseResponse<String> publicComment(@RequestBody CommentDto commentDto) {

        log.info("当前用户正在发表评论,评论内容为"+commentDto.getContent());
        //对评论进行非空判断
        if(commentDto.getContent() == null)
        {
            return ResultUtils.error(ErrorCode.NULL_COMMENT);
        }

        try {
            userCommentService.insertComment(commentDto);
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统内部出错，评论发表失败");
        }
        return ResultUtils.success("评论发表成功");

    }

    /**
     * 用户删除回复
     */
    @DeleteMapping ("/delComment")
    public BaseResponse<String> publicComment(@RequestParam  Integer CommentId) {
        log.info("当前用户正在尝试删除自己的评论，评论id为 "+CommentId);
        try {
            userCommentService.deleteComment(CommentId);
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统内部出错，评论删除失败");
        }
        return ResultUtils.success("评论已经删除成功");
    }

    /**
     * 给电影评分
     */


}
