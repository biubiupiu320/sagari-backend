package com.sagari.service.feign;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author biubiupiu~
 */
@FeignClient("sagari-tag-service")
public interface TagServiceFeign {

    @GetMapping("/getFollowTags")
    public BaseResponse<JSONObject> getFollowTags(@RequestParam("userId") Integer userId);
}
