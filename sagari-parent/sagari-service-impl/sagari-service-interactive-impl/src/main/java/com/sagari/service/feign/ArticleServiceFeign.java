package com.sagari.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author biubiupiu~
 */
@FeignClient(value = "sagari-article-service")
public interface ArticleServiceFeign {

    @GetMapping(value = "/isExist")
    public Boolean isExist(@RequestParam("articleId") Integer articleId);

    @GetMapping(value = "/incrementGood")
    public Boolean incrementGood(@RequestParam("articleId") Integer articleId);

    @GetMapping(value = "/decreaseGood")
    public Boolean decreaseGood(@RequestParam("articleId") Integer articleId);
}