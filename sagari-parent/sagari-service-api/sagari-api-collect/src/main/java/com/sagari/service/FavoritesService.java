package com.sagari.service;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.FavoritesInputDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author biubiupiu~
 */
@Api(tags = "收藏夹服务接口")
public interface FavoritesService {

    @ApiOperation(value = "创建收藏夹接口")
    @PutMapping("/createFavorites")
    public BaseResponse<JSONObject> createFavorites(@RequestBody @Valid FavoritesInputDTO favoritesInputDTO,
                                                    BindingResult bindingResult);

    @ApiOperation(value = "修改收藏夹接口")
    @PostMapping("/modifyFavorites")
    public BaseResponse<JSONObject> modifyFavorites(@RequestBody @Valid FavoritesInputDTO favoritesInputDTO,
                                                    BindingResult bindingResult);

    @ApiOperation(value = "删除收藏夹接口")
    @PostMapping("/deleteFavorites")
    public BaseResponse<JSONObject> deleteFavorites(@RequestParam(name = "id") Integer id);

    @ApiOperation(value = "获取收藏夹接口")
    @GetMapping("/getFavorites")
    public BaseResponse<JSONObject> getFavorites(@RequestParam(name = "targetUserId") Integer targetUserId);

    @ApiOperation(value = "获取公开收藏夹接口")
    @GetMapping("/getPubFavorites")
    public BaseResponse<JSONObject> getPubFavorites(@RequestParam(name = "targetUserId") Integer targetUserId);

    @ApiOperation(value = "获取私密收藏夹接口")
    @GetMapping("/getPriFavorites")
    public BaseResponse<JSONObject> getPriFavorites(@RequestParam(name = "targetUserId") Integer targetUserId);

}
