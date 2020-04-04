package com.sagari.service;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.TagInputDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author biubiupiu~
 */
@Api(tags = "标签服务接口")
public interface TagService {

    @ApiOperation(value = "创建标签接口")
    @PutMapping("/createTag")
    public BaseResponse<JSONObject> createTag(@RequestBody @Valid TagInputDTO tagInputDTO,
                                              BindingResult bindingResult);

    @ApiOperation(value = "获取所有标签接口")
    @GetMapping("/getTag")
    public BaseResponse<JSONObject> getTag();

    @ApiOperation(value = "获取热门标签接口")
    @GetMapping("/getHotTag")
    public BaseResponse<JSONObject> getHotTag();

    @ApiOperation(value = "根据标签ID列表批量获取标签接口")
    @PostMapping("/getTagBatch")
    public BaseResponse<JSONObject> getTagBatch(@RequestBody List<Integer> tagIds);

    @ApiOperation(value = "增加标签的文章数量")
    @PostMapping("/incrArticleCount")
    public BaseResponse<JSONObject> incrArticleCount(@RequestBody List<Integer> tagIds);

}
