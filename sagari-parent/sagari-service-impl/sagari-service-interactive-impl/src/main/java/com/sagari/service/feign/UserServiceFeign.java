package com.sagari.service.feign;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author biubiupiu~
 */
@FeignClient(value = "sagari-user-service")
public interface UserServiceFeign {
    @GetMapping("/isExist")
    public Boolean isExist(@RequestParam(name = "userId") Integer userId);

    @GetMapping("/incrementFollowCount")
    public Boolean incrementFollowCount(@RequestParam(name = "id") Integer id);

    @GetMapping("/decreaseFollowCount")
    public Boolean decreaseFollowCount(@RequestParam(name = "id") Integer id);

    @GetMapping("/incrementFollowCountN")
    public Boolean incrementFollowCountN(@RequestParam(name = "id") Integer id,
                                         @RequestParam(name = "count") Integer count);

    @GetMapping("/decreaseFollowCountN")
    public Boolean decreaseFollowCountN(@RequestParam(name = "id") Integer id,
                                        @RequestParam(name = "count") Integer count);

    @GetMapping("/incrementFansCount")
    public Boolean incrementFansCount(@RequestParam(name = "id") Integer id);

    @GetMapping("/decreaseFansCount")
    public Boolean decreaseFansCount(@RequestParam(name = "id") Integer id);

    @GetMapping("/incrementFansCountN")
    public Boolean incrementFansCountN(@RequestParam(name = "id") Integer id,
                                       @RequestParam(name = "count") Integer count);

    @GetMapping("/decreaseFansCountN")
    public Boolean decreaseFansCountN(@RequestParam(name = "id") Integer id,
                                      @RequestParam(name = "count") Integer count);

    @PostMapping("/incrementFansCountBatch")
    public Boolean incrementFansCountBatch(@RequestBody List<Integer> ids);

    @PostMapping("/decreaseFansCountBatch")
    public Boolean decreaseFansCountBatch(@RequestBody List<Integer> ids);

    @PostMapping("/incrementFollowCountBatch")
    public Boolean incrementFollowCountBatch(@RequestBody List<Integer> ids);

    @PostMapping("/decreaseFollowCountBatch")
    public Boolean decreaseFollowCountBatch(@RequestBody List<Integer> ids);

    @PostMapping("/getSimpleUserByList")
    public BaseResponse<JSONObject> getSimpleUserByList(@RequestBody List<Integer> ids);
}

