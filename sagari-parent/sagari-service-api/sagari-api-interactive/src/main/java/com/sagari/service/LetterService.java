package com.sagari.service;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author biubiupiu~
 */
@Api(tags = "私信服务接口")
public interface LetterService {

    @ApiOperation(value = "获取私信人列表")
    @GetMapping("/getPersonList")
    public BaseResponse<JSONObject> getPersonList(@RequestParam(name = "page") Integer page,
                                                  @RequestParam(name = "size") Integer size);

    @ApiOperation(value = "获取私信列表")
    @GetMapping("/getLetters")
    public BaseResponse<JSONObject> getLetters(@RequestParam(name = "toId") Integer toId,
                                               @RequestParam(name = "page") Integer page,
                                               @RequestParam(name = "size") Integer size);

}
