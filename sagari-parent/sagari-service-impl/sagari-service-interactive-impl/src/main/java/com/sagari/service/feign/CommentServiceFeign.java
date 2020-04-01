package com.sagari.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author biubiupiu~
 */
@FeignClient("sagari-comment-service")
public interface CommentServiceFeign {
    @GetMapping("/incrementGood")
    public Boolean incrementGood(@RequestParam("id") Integer id, @RequestParam("type") Boolean type);

    @GetMapping("/decreaseGood")
    public Boolean decreaseGood(@RequestParam("id") Integer id, @RequestParam("type") Boolean type);
}
