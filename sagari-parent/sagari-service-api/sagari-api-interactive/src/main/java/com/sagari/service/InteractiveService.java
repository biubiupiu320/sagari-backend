package com.sagari.service;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.FansIds;
import com.sagari.dto.input.FollowIds;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author biubiupiu~
 */
@Api(tags = "点赞服务接口")
public interface InteractiveService {

    @ApiOperation(value = "点赞或取消点赞接口")
    @GetMapping("/good")
    public BaseResponse<JSONObject> toggleGood(@RequestParam(name = "targetId") Integer targetId,
                                               @RequestParam(name = "author") Integer author,
                                               @RequestParam(name = "articleId") Integer articleId,
                                               @RequestParam(name = "type") Integer type,
                                               @RequestParam(name = "toUserId", required = false) Integer toUserId);

    @ApiOperation(value = "获取用户是否点赞接口")
    @GetMapping("/getInteractive")
    public Boolean isGood(@RequestParam(name = "targetId") Integer targetId,
                          @RequestParam(name = "userId") Integer userId,
                          @RequestParam(name = "type") Integer type);

    @ApiOperation(value = "关注用户接口")
    @PutMapping("/follow")
    public BaseResponse<JSONObject> follow(@RequestParam(name = "followId") Integer followId);

    @ApiOperation(value = "取消关注用户接口，支持批量")
    @PostMapping("/cancelFollow")
    public BaseResponse<JSONObject> cancelFollow(@RequestBody FollowIds follows);

    @ApiOperation(value = "获取是否关注用户接口")
    @GetMapping("/isFollow")
    public Boolean isFollow(@RequestParam(name = "followId") Integer followId);

    @ApiOperation(value = "移除粉丝接口，支持批量")
    @PostMapping("/removeFans")
    public BaseResponse<JSONObject> removeFans(@RequestBody FansIds fanss);

    @ApiOperation(value = "获取关注列表")
    @GetMapping("/getFollowList")
    public BaseResponse<JSONObject> getFollowList(@RequestParam(name = "fansId", required = false) Integer fansId,
                                                  @RequestParam(name = "page") Integer page,
                                                  @RequestParam(name = "size") Integer size);

    @ApiOperation(value = "获取粉丝列表")
    @GetMapping("/getFansList")
    public BaseResponse<JSONObject> getFansList(@RequestParam(name = "followId", required = false) Integer followId,
                                                @RequestParam(name = "page") Integer page,
                                                @RequestParam(name = "size") Integer size);

}
