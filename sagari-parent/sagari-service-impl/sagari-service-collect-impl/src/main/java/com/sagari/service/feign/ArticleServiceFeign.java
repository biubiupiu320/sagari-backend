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
@FeignClient(value = "sagari-article-service")
public interface ArticleServiceFeign {

    @PostMapping(value = "/article")
    public BaseResponse<JSONObject> selectArticleList(@RequestBody List<Integer> articleIds);

    @GetMapping(value = "/incrementCollect")
    public Boolean incrementCollect(@RequestParam("articleId") Integer articleId);

    @GetMapping(value = "/decreaseCollect")
    public Boolean decreaseCollect(@RequestParam("articleId") Integer articleId);

    @PostMapping(value = "/incrementCollectN")
    public Boolean incrementCollectN(@RequestBody List<Integer> ids);

    @PostMapping(value = "/decreaseCollectN")
    public Boolean decreaseCollectN(@RequestBody List<Integer> ids);

}
