package com.sagari.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author biubiupiu~
 */
@FeignClient(value = "sagari-interactive-service")
public interface InteractiveServiceFeign {

    @GetMapping("/getInteractive")
    public Boolean isGood(@RequestParam(name = "targetId") Integer targetId,
                          @RequestParam(name = "userId") Integer userId,
                          @RequestParam(name = "type") Integer type);
}
