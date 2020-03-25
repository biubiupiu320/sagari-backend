package com.sagari.service;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.ArticleInputDTO;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author biubiupiu~
 */
@Api(tags = "文章服务接口")
public interface ArticleService {

    @ApiOperation(value = "发布文章接口")
    @PutMapping(value = "/article")
    public BaseResponse<JSONObject> publishArticle(@RequestBody @Validated ArticleInputDTO articleInputDTO,
                                                   BindingResult bindingResult);

    @ApiOperation(value = "获取文章接口")
    @GetMapping(value = "/article/{articleId}")
    public BaseResponse<JSONObject> selectArticle(@PathVariable("articleId") Integer articleId);

    @ApiOperation(value = "修改文章接口")
    @PostMapping(value = "/article/{articleId}")
    public BaseResponse<JSONObject> editArticle(@PathVariable("articleId") Integer articleId,
                                                @RequestBody ArticleInputDTO articleInputDTO);

    @ApiOperation(value = "删除文章接口")
    @DeleteMapping(value = "/article/{articleId}")
    public BaseResponse<JSONObject> deleteArticle(@PathVariable("articleId") Integer articleId);

    @ApiOperation(value = "检查文章是否是本人发布接口")
    @GetMapping(value = "/article/checkPermissions")
    public BaseResponse<JSONObject> checkPermissions(@RequestParam("articleId") Integer articleId,
                                                     @RequestParam("creator") Integer creator);

    @ApiOperation(value = "判断文章是否存在接口")
    @GetMapping(value = "/isExist")
    public Boolean isExist(@RequestParam("articleId") Integer articleId);

    @ApiOperation(value = "文章点赞数量+1接口")
    @GetMapping(value = "/incrementGood")
    public Boolean incrementGood(@RequestParam("articleId") Integer articleId);

    @ApiOperation(value = "文章点赞数量-1接口")
    @GetMapping(value = "/decreaseGood")
    public Boolean decreaseGood(@RequestParam("articleId") Integer articleId);

    @ApiOperation(value = "获取文章作者ID")
    @GetMapping(value = "/getAuthor")
    public Integer getAuthor(@RequestParam("articleId") Integer articleId);

    @ApiOperation(value = "文章评论数量+1接口")
    @GetMapping(value = "/incrementComment")
    public Boolean incrementComment(@RequestParam("articleId") Integer articleId);

    @ApiOperation(value = "文章点赞数量-1接口")
    @GetMapping(value = "/decreaseComment")
    public Boolean decreaseComment(@RequestParam("articleId") Integer articleId);

    @ApiOperation(value = "文章阅读数量+1接口")
    @GetMapping(value = "/incrementView")
    public Boolean incrementView(@RequestParam("article") Integer articleId);

    @ApiOperation(value = "文章收藏数量+1接口")
    @GetMapping(value = "/incrementCollect")
    public Boolean incrementCollect(@RequestParam("articleId") Integer articleId);

    @ApiOperation(value = "文章收藏数量-1接口")
    @GetMapping(value = "/decreaseCollect")
    public Boolean decreaseCollect(@RequestParam("articleId") Integer articleId);
}
