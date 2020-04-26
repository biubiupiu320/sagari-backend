package com.sagari.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 * @author biubiupiu~
 */
@FeignClient(value = "sagari-search-service")
public interface SearchServiceFeign {

    @GetMapping("/deleteArticle")
    public Boolean deleteArticle(@RequestParam(name = "articleId") Integer articleId,
                                 @RequestParam(name = "userId") Integer userId) throws IOException;
}
