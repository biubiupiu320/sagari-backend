package com.sagari.service.feign;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author biubiupiu~
 */
@FeignClient("sagari-tag-service")
public interface TagServiceFeign {

    @PostMapping("/getTagBatch")
    public BaseResponse<JSONObject> getTagBatch(@RequestBody List<Integer> tagIds);

    @PostMapping("/incrArticleCount")
    public BaseResponse<JSONObject> incrArticleCount(@RequestBody List<Integer> tagIds);
}
