package com.sagari.service;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.CollectBatchInputDTO;
import com.sagari.dto.input.CollectInputDTO;
import com.sagari.dto.input.MoveBatchInputDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author biubiupiu~
 */
@Api(tags = "收藏服务接口")
public interface CollectService {

    @ApiOperation(value = "收藏文章接口")
    @PutMapping("/collectArticle")
    public BaseResponse<JSONObject> collectArticle(@RequestBody @Valid CollectInputDTO collectInputDTO,
                                                   BindingResult bindingResult);

    @ApiOperation(value = "取消收藏文章接口,支持批量")
    @PostMapping("/cancelCollectArticle")
    public BaseResponse<JSONObject> cancelCollectArticle(@RequestBody @Valid CollectBatchInputDTO collectBatchInputDTO,
                                                         BindingResult bindingResult);

    @ApiOperation(value = "移动收藏文章至其他收藏夹,支持批量")
    @PostMapping("/moveToFavorites")
    public BaseResponse<JSONObject> moveToFavorites(@RequestBody @Valid MoveBatchInputDTO moveBatchInputDTO,
                                                    BindingResult bindingResult);

    @ApiOperation(value = "获取收藏夹文章")
    @GetMapping("/getCollect")
    public BaseResponse<JSONObject> getCollect(@RequestParam(name = "favoritesId") Integer favoritesId,
                                               @RequestParam(name = "page") Integer page,
                                               @RequestParam(name = "size") Integer size);

    @ApiOperation(value = "检查用户是否收藏了该文章")
    @GetMapping("/isCollect")
    public Boolean isCollect(@RequestParam(name = "articleId") Integer articleId,
                             @RequestParam(name = "userId", required = false) Integer userId);
}
