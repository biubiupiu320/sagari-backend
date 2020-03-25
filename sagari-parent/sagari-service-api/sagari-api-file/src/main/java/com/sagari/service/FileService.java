package com.sagari.service;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author biubiupiu~
 */
@Api(tags = "上传文件服务接口")
public interface FileService {

    @ApiOperation(value = "上传文件接口")
    @PostMapping("/upload")
    public BaseResponse<JSONObject> upload(@RequestParam(value = "file")MultipartFile file,
                                           @RequestParam(value = "type")Integer type);

    @PostMapping("/insert-image")
    public String upload(HttpServletRequest request);
}
