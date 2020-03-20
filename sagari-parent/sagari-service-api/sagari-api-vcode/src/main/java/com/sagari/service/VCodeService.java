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
@Api(tags = "验证码服务接口")
public interface VCodeService {

    @ApiOperation(value = "获取验证码")
    @GetMapping("/getCode")
    BaseResponse<JSONObject> getCode(@RequestParam(name = "phone") String phone,
                                     @RequestParam(name = "type") String type,
                                     @RequestParam(name = "random") String random);

    @ApiOperation(value = "校验验证码")
    @GetMapping("/verifyCode")
    BaseResponse<JSONObject> verifyCode(@RequestParam(name = "phone") String phone,
                                        @RequestParam(name = "type") String type,
                                        @RequestParam(name = "vcode") String vcode);
}
