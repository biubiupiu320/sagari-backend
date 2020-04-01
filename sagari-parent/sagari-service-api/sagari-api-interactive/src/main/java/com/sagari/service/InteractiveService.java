package com.sagari.service;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author biubiupiu~
 */
@Api(tags = "点赞服务接口")
public interface InteractiveService {

    @ApiOperation(value = "点赞或取消点赞接口")
    @GetMapping("/good")
    public BaseResponse<JSONObject> toggleGood(@RequestParam(name = "targetId") Integer targetId,
                                               @RequestParam(name = "userId") Integer userId,
                                               @RequestParam(name = "type") Integer type);

    @ApiOperation(value = "获取用户是否点赞接口")
    @GetMapping("/getInteractive")
    public Boolean isGood(@RequestParam(name = "targetId") Integer targetId,
                          @RequestParam(name = "userId") Integer userId,
                          @RequestParam(name = "type") Integer type);
}
