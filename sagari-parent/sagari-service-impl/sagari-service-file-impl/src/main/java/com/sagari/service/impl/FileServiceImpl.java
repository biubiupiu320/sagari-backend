package com.sagari.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * @author biubiupiu~
 */
@RestController
@Slf4j
public class FileServiceImpl extends BaseApiService<JSONObject> implements FileService {

    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.access-key}")
    private String accessKey;
    @Value("${oss.access-secret}")
    private String accessSecret;
    @Value("${oss.bucket-name}")
    private String bucketName;
    @Value("${oss.avatar}")
    private String avatar;
    @Value("${oss.article}")
    private String article;
    @Value("${oss.letter}")
    private String letter;
    @Value("${oss.expires}")
    private Long expires;

    @Override
    public BaseResponse<JSONObject> upload(MultipartFile file, Integer type) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, accessSecret);
        URL url = null;
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String filename = System.currentTimeMillis() + "";
            Date expiresTime = new Date(Long.parseLong(filename) + expires);
            if (type.equals(1)) {
                ossClient.putObject(bucketName, avatar + filename, inputStream);
                url = ossClient.generatePresignedUrl(bucketName, avatar + filename, expiresTime);
            } else if (type.equals(2)) {
                ossClient.putObject(bucketName, article + filename, inputStream);
                url = ossClient.generatePresignedUrl(bucketName, article + filename, expiresTime);
            } else if (type.equals(3)) {
                ossClient.putObject(bucketName, letter + filename, inputStream);
                url = ossClient.generatePresignedUrl(bucketName, letter + filename, expiresTime);
            } else {
                ossClient.shutdown();
                return setResultError("无效的请求");
            }
            ossClient.shutdown();
        } catch (IOException e) {
            return setResultError("上传文件失败");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        JSONObject result = new JSONObject();
        result.put("url", url.toString());
        return setResultSuccess(result);
    }

    @Override
    public String upload(HttpServletRequest request) {
        Integer type = Integer.valueOf(request.getParameter("type"));
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("editormd-image-file");
        JSONObject result = new JSONObject();
        if (file != null) {
            BaseResponse<JSONObject> jsonObject = upload(file, type);
            JSONObject data = jsonObject.getData();
            if (jsonObject.getCode().equals(200)) {
                result.put("success", 1);
            } else {
                result.put("success", 0);
            }
            result.put("message", jsonObject.getMsg());
            result.put("url", data.getString("url"));
            return "<div id='data'>" + result.toJSONString() + "</div>" +
                    "<script>" +
                    "   window.onmessage=function (ev) {" +
                    "       var data=document.getElementById('data').innerText;" +
                    "       data=JSON.parse(data);" +
                    "       ev.source.postMessage(data, ev.origin);" +
                    "   }" +
                    "</script>";
        }
        result.put("success", 0);
        result.put("message", "上传失败");
        result.put("url", null);
        return "<div id='data'>" + result.toJSONString() + "</div>" +
                "<script>" +
                "   window.onmessage=function (ev) {" +
                "       var data=document.getElementById('data').innerText;" +
                "       data=JSON.parse(data);" +
                "       ev.source.postMessage(data, ev.origin);" +
                "   }" +
                "</script>";
    }
}
