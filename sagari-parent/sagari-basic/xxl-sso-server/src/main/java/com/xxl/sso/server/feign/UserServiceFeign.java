package com.xxl.sso.server.feign;


import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author biubiupiu~
 */
@FeignClient(value = "sagari-user-service")
public interface UserServiceFeign {

    @GetMapping("/sign-in")
    public BaseResponse<JSONObject> signIn(@RequestParam(name = "account") String account,
                                           @RequestParam(name = "password") String password);

    @GetMapping("/getSimpleUser")
    public BaseResponse<JSONObject> getSimpleUser(@RequestParam(name = "id") Integer id);
}
