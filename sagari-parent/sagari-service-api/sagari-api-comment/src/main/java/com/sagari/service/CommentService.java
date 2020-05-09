package com.sagari.service;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.ChildCommentDTO;
import com.sagari.dto.input.ParentCommentDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author biubiupiu~
 */
@Api(tags = "评论服务接口")
public interface CommentService {

    @ApiOperation(value = "生成父评论接口")
    @PostMapping("/comment-parent")
    public BaseResponse<JSONObject> insertParentComment(@RequestBody @Valid ParentCommentDTO parentCommentDTO,
                                                        BindingResult bindingResult);

    @ApiOperation(value = "删除父评论接口")
    @DeleteMapping("/comment-parent/{commentId}")
    public BaseResponse<JSONObject> deleteParentComment(@PathVariable("commentId") Integer id,
                                                        @RequestParam("articleId") Integer articleId);

    @ApiOperation(value = "获取评论接口")
    @GetMapping("/comment/{articleId}")
    public BaseResponse<JSONObject> getComment(@PathVariable("articleId") Integer articleId,
                                               @RequestParam(value = "userId", required = false) Integer userId,
                                               @RequestParam("page") Integer page,
                                               @RequestParam("size") Integer size);

    @ApiOperation(value = "生成子评论接口")
    @PostMapping("/comment-child")
    public BaseResponse<JSONObject> insertChildComment(@RequestBody @Valid ChildCommentDTO childCommentDTO,
                                                       BindingResult bindingResult);

    @ApiOperation(value = "删除子评论接口")
    @DeleteMapping("/comment-child/{commentId}")
    public BaseResponse<JSONObject> deleteChildComment(@PathVariable("commentId") Integer id,
                                                       @RequestParam("articleId") Integer articleId,
                                                       @RequestParam("parentId") Integer parentId);

    @ApiOperation(value = "只获取子评论接口")
    @GetMapping("/getChildComment")
    public BaseResponse<JSONObject> getChildComment(@RequestParam("parentId") Integer parentId,
                                                    @RequestParam(value = "userId", required = false) Integer userId,
                                                    @RequestParam("offset") Integer offset,
                                                    @RequestParam("limit") Integer limit);

    @GetMapping("/incrementGood")
    public Boolean incrementGood(@RequestParam("id") Integer id, @RequestParam("type") Boolean type);

    @GetMapping("/decreaseGood")
    public Boolean decreaseGood(@RequestParam("id") Integer id, @RequestParam("type") Boolean type);

    @PostMapping("/getParentContent")
    public BaseResponse<JSONObject> getParentContent(@RequestBody List<Integer> ids);

    @PostMapping("/getChildContent")
    public BaseResponse<JSONObject> getChildContent(@RequestBody List<Integer> ids);
}
