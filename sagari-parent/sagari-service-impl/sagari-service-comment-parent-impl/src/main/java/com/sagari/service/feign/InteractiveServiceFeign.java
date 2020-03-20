package com.sagari.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author biubiupiu~
 */
@FeignClient("sagari-interactive-service")
public interface InteractiveServiceFeign {
    @PostMapping("/getInteractive")
    public Map<Integer, Boolean> isGood(@RequestBody List<Integer> list,
                                        @RequestParam(name = "userId") Integer userId,
                                        @RequestParam(name = "type") Integer type);
}
