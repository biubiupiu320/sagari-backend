package com.sagari.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author biubiupiu~
 */
@FeignClient(value = "sagari-user-service")
public interface UserServiceFeign {
    @GetMapping("/isExist")
    public Boolean isExist(@RequestParam(name = "userId") Integer userId);
}

