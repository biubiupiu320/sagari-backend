package com.sagari.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author biubiupiu~
 */
@FeignClient(value = "sagari-collect-service")
public interface CollectServiceFeign {

    @GetMapping("/isCollect")
    public Boolean isCollect(@RequestParam("articleId") Integer articleId,
                             @RequestParam("userId") Integer userId);
}
