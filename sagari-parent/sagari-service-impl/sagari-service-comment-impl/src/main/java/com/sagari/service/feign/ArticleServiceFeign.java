package com.sagari.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author biubiupiu~
 */
@FeignClient("sagari-article-service")
public interface ArticleServiceFeign {
    @GetMapping(value = "/isExist")
    public Boolean isExist(@RequestParam("articleId") Integer articleId);

    @GetMapping(value = "/getAuthor")
    public Integer getAuthor(@RequestParam("articleId") Integer articleId);

    @GetMapping(value = "/incrementComment")
    public Boolean incrementComment(@RequestParam("articleId") Integer articleId);

    @GetMapping(value = "/decreaseComment")
    public Boolean decreaseComment(@RequestParam("articleId") Integer articleId);
}
