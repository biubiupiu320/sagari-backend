package com.sagari.service;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.NoticeCommentDTO;
import com.sagari.dto.input.NoticeFollowDTO;
import com.sagari.dto.input.NoticeGoodDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author biubiupiu~
 */
@Api(tags = "通知服务接口")
public interface NoticeService {

    @ApiOperation(value = "获取评论通知")
    @GetMapping("/noticeComment")
    public BaseResponse<JSONObject> getNoticeComment(@RequestParam(name = "page") Integer page,
                                                     @RequestParam(name = "size") Integer size);

    @ApiOperation(value = "获取关注通知")
    @GetMapping("/noticeFollow")
    public BaseResponse<JSONObject> getNoticeFollow(@RequestParam(name = "page") Integer page,
                                                    @RequestParam(name = "size") Integer size);

    @ApiOperation(value = "获取点赞通知")
    @GetMapping("/noticeGood")
    public BaseResponse<JSONObject> getNoticeGood(@RequestParam(name = "page") Integer page,
                                                  @RequestParam(name = "size") Integer size);

    @ApiOperation(value = "生成评论通知")
    @PutMapping("/noticeComment")
    public Boolean noticeComment(@RequestBody NoticeCommentDTO noticeCommentDTO);

    @ApiOperation(value = "生成关注通知")
    @PutMapping("/noticeFollow")
    public Boolean noticeFollow(@RequestBody NoticeFollowDTO noticeFollowDTO);

    @ApiOperation(value = "生成点赞通知")
    @PutMapping("/noticeGood")
    public Boolean noticeGood(@RequestBody NoticeGoodDTO noticeGoodDTO);

    @ApiOperation(value = "将评论通知标记为已读")
    @PostMapping("/noticeComment")
    public BaseResponse<JSONObject> markReadComment(@RequestBody List<Integer> ids);

    @ApiOperation(value = "将关注通知标记为已读")
    @PostMapping("/noticeFollow")
    public BaseResponse<JSONObject> markReadFollow(@RequestBody List<Integer> ids);

    @ApiOperation(value = "将点赞通知标记为已读")
    @PostMapping("/noticeGood")
    public BaseResponse<JSONObject> markReadGood(@RequestBody List<Integer> ids);

    @ApiOperation(value = "删除评论通知消息")
    @DeleteMapping("/noticeComment")
    public BaseResponse<JSONObject> delNoticeComment(@RequestBody List<Integer> ids);

    @ApiOperation(value = "删除关注通知消息")
    @DeleteMapping("/noticeFollow")
    public BaseResponse<JSONObject> delNoticeFollow(@RequestBody List<Integer> ids);

    @ApiOperation(value = "删除点赞通知消息")
    @DeleteMapping("/noticeGood")
    public BaseResponse<JSONObject> delNoticeGood(@RequestBody List<Integer> ids);

    @ApiOperation(value = "将所有未读的评论通知标记为已读")
    @PostMapping("/noticeCommentAll")
    public BaseResponse<JSONObject> markReadCommentAll();

    @ApiOperation(value = "将所有未读的关注通知标记为已读")
    @PostMapping("/noticeFollowAll")
    public BaseResponse<JSONObject> markReadFollowAll();

    @ApiOperation(value = "将所有未读的点赞通知标记为已读")
    @PostMapping("/noticeGoodAll")
    public BaseResponse<JSONObject> markReadGoodAll();

    @ApiOperation(value = "删除所有评论通知")
    @DeleteMapping("/noticeCommentAll")
    public BaseResponse<JSONObject> delNoticeCommentAll();

    @ApiOperation(value = "删除所有关注通知")
    @DeleteMapping("/noticeFollowAll")
    public BaseResponse<JSONObject> delFollowCommentAll();

    @ApiOperation(value = "删除所有点赞通知")
    @DeleteMapping("/noticeGoodAll")
    public BaseResponse<JSONObject> delGoodCommentAll();

    @ApiOperation(value = "获取系统通知")
    @GetMapping("/getNoticeSystem")
    public BaseResponse<JSONObject> getNoticeSystem(@RequestParam(name = "page") Integer page,
                                                    @RequestParam(name = "size") Integer size);

    @ApiOperation(value = "将系统通知标记为已读")
    @PutMapping("/markReadSystem")
    public BaseResponse<JSONObject> markReadSystem(@RequestBody List<Integer> ids);

    @ApiOperation(value = "将所有未读的系统通知标记为已读")
    @PutMapping("/markReadSystemAll")
    public BaseResponse<JSONObject> markReadSystemAll();

    @ApiOperation(value = "创建系统通知")
    @PutMapping("/createNoticeSystem")
    public BaseResponse<JSONObject> createNoticeSystem(@RequestParam(name = "title") String title,
                                                       @RequestParam(name = "content") String content);

    @ApiOperation(value = "获取未读的通知条数，包括评论、关注、点赞和系统通知")
    @GetMapping("/unreadNoticeCount")
    public BaseResponse<JSONObject> getUnreadCount();
}
