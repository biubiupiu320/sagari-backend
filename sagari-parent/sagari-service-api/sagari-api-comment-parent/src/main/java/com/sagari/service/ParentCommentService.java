package com.sagari.service;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.ParentCommentDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author biubiupiu~
 */
@Api(tags = "父评论服务接口")
public interface ParentCommentService {

    @ApiOperation(value = "生成父评论接口")
    @PostMapping("/comment-parent")
    public BaseResponse<JSONObject> insertComment(@RequestBody @Valid ParentCommentDTO parentCommentDTO,
                                                  BindingResult bindingResult);

    @ApiOperation(value = "删除父评论接口")
    @DeleteMapping("/comment-parent/{commentId}")
    public BaseResponse<JSONObject> deleteComment(@PathVariable("commentId") Integer id,
                                                  @RequestParam("userId") Integer userId);

    @ApiOperation(value = "获取父评论接口")
    @GetMapping("/comment-parent/{articleId}")
    public BaseResponse<JSONObject> getComment(@PathVariable("articleId") Integer articleId,
                                               @RequestParam("userId") Integer userId,
                                               @RequestParam("page") Integer page,
                                               @RequestParam("size") Integer size);
}
