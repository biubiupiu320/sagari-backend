package com.sagari.service.feign;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author biubiupiu~
 */
@FeignClient(value = "sagari-user-service")
public interface UserServiceFeign {

    @PostMapping("/getSimpleUserByList")
    public BaseResponse<JSONObject> getSimpleUserByList(@RequestBody List<Integer> ids);
}
