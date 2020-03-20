package com.sagari.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.common.utils.RegexUtils;
import com.sagari.service.VCodeService;
import com.sagari.service.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author biubiupiu~
 */
@RestController
public class VCodeServiceImpl extends BaseApiService<JSONObject> implements VCodeService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    @GetMapping("/getCode")
    public BaseResponse<JSONObject> getCode(@RequestParam(name = "phone") String phone,
                                            @RequestParam(name = "type") String type,
                                            @RequestParam(name = "random") String random) {
        if (!RegexUtils.checkMobile(phone)) {
            return setResultError("无效的手机号码");
        }
        if (Integer.parseInt(type) != 1 && Integer.parseInt(type) != 2) {
            return setResultError("不受支持的验证码请求类型");
        }
        String uuid = UUID.randomUUID().toString();
        if (Integer.parseInt(type) == 1) {
            if (StringUtils.isBlank(redisUtil.getString(phone + ".signinlast"))) {
                //注册验证码
                redisUtil.setString(phone + ".signin", uuid, 1800L);
                redisUtil.setString(phone + ".signinlast", "true", 60L);
            } else {
                return setResultError("您在1分钟内已经获取过验证码了");
            }
        } else if (Integer.parseInt(type) == 2) {
            if (StringUtils.isBlank(redisUtil.getString(phone + ".modifypasswordlast"))) {
                //修改密码验证码
                redisUtil.setString(phone + ".modifypassword", uuid, 1800L);
                redisUtil.setString(phone + ".modifypasswordlast", "true", 60L);
            } else {
                return setResultError("您在1分钟内已经获取过验证码了");
            }
        }
        return setResultSuccess(uuid);
    }

    @Override
    @GetMapping("/verifyCode")
    public BaseResponse<JSONObject> verifyCode(@RequestParam(name = "phone") String phone,
                                               @RequestParam(name = "type") String type,
                                               @RequestParam(name = "vcode") String vcode) {
        if (!RegexUtils.checkMobile(phone)) {
            return setResultError("无效的手机号码");
        }
        String code;
        if (Integer.parseInt(type) == 1) {
            code = redisUtil.getString(phone + ".signin");
        } else if (Integer.parseInt(type) == 2) {
            code = redisUtil.getString(phone + ".modifypassword");
        } else {
            return setResultError("不受支持的验证码请求类型");
        }
        if (vcode.equals(code)) {
            if (Integer.parseInt(type) == 1) {
                if (!redisUtil.delKey(phone + ".signin")) {
                    return setResultError("服务器刚才打了个盹儿，请再试一次吧");
                }
            } else if (Integer.parseInt(type) == 2) {
                if (!redisUtil.delKey(phone + ".modifypassword")) {
                    return setResultError("服务器刚才打了个盹儿，请再试一次吧");
                }
            }
            return setResultSuccess("验证码正确");
        }
        return setResultError("验证码错误或已过期");
    }
}
