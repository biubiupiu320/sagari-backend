package com.sagari.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.common.utils.RegexUtils;
import com.sagari.service.VCodeService;
import com.sagari.service.util.RedisUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @author biubiupiu~
 */
@RestController
public class VCodeServiceImpl extends BaseApiService<JSONObject> implements VCodeService {

    @Autowired
    private RedisUtil redisUtil;
    @Value("${vcode.expires}")
    private Long exipres;
    @Value("${sms.domain}")
    private String domain;
    @Value("${sms.template-id}")
    private String[] templateId;
    @Value("${sms.sdk-id}")
    private String sdkId;
    @Value("${sms.sign}")
    private String sign;
    @Value("${sms.secret-id}")
    private String secretId;
    @Value("${sms.secret-key}")
    private String secretKey;
    @Value("${sms.expires}")
    private String smsExpires;

    @Override
    @GetMapping("/getCode")
    public BaseResponse<JSONObject> getCode(@RequestParam(name = "phone") String phone,
                                            @RequestParam(name = "type") Integer type,
                                            @RequestParam(name = "random") String random) {
        if (!RegexUtils.checkMobile(phone)) {
            return setResultError("无效的手机号码");
        }
        if (!type.equals(1) && !type.equals(2)) {
            return setResultError("不受支持的验证码请求类型");
        }
        String vCode = String.valueOf(new Random().nextInt(899999) + 100000);
        try{
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(domain);
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, "", clientProfile);

            StringBuffer params = new StringBuffer("{\"PhoneNumberSet\":[\"+86");
            params.append(phone).append("\"],");
            params.append("\"TemplateID\":\"");
            if (type.equals(1)) {
                params.append(templateId[0]).append("\",");
            } else {
                params.append(templateId[1]).append("\",");
            }
            params.append("\"Sign\":\"");
            params.append(sign).append("\",");
            params.append("\"TemplateParamSet\":[\"");
            params.append(vCode).append("\",\"");
            params.append(smsExpires).append("\"],");
            params.append("\"SmsSdkAppid\":\"");
            params.append(sdkId).append("\"}");
            System.out.println(params.toString());
            SendSmsRequest req = SendSmsRequest.fromJsonString(params.toString(), SendSmsRequest.class);
            SendSmsResponse resp = client.SendSms(req);
            System.out.println(SendSmsRequest.toJsonString(resp));
            if (resp.getSendStatusSet()[0].getCode().equalsIgnoreCase("OK")) {
                if (type.equals(1)) {
                    if (StringUtils.isBlank(redisUtil.getString(phone + ".signinlast"))) {
                        //注册验证码
                        redisUtil.setString(phone + ".signin", vCode, exipres);
                        redisUtil.setString(phone + ".signinlast", "true", 60L);
                    } else {
                        return setResultError("您在1分钟内已经获取过验证码了");
                    }
                } else {
                    if (StringUtils.isBlank(redisUtil.getString(phone + ".modifypasswordlast"))) {
                        //修改密码验证码
                        redisUtil.setString(phone + ".modifypassword", vCode, exipres);
                        redisUtil.setString(phone + ".modifypasswordlast", "true", 60L);
                    } else {
                        return setResultError("您在1分钟内已经获取过验证码了");
                    }
                }
                return setResultSuccess("获取验证码成功");
            } else {
                return setResultError("获取验证码失败");
            }
        } catch (TencentCloudSDKException e) {
            return setResultError("获取验证码失败");
        }
    }

    @Override
    @GetMapping("/verifyCode")
    public BaseResponse<JSONObject> verifyCode(@RequestParam(name = "phone") String phone,
                                               @RequestParam(name = "type") Integer type,
                                               @RequestParam(name = "vcode") String vcode) {
        if (!RegexUtils.checkMobile(phone)) {
            return setResultError("无效的手机号码");
        }
        String code;
        if (type.equals(1)) {
            code = redisUtil.getString(phone + ".signin");
        } else if (type.equals(2)) {
            code = redisUtil.getString(phone + ".modifypassword");
        } else {
            return setResultError("不受支持的验证码请求类型");
        }
        if (vcode.equals(code)) {
            if (type.equals(1)) {
                if (!redisUtil.delKey(phone + ".signin")) {
                    return setResultError("服务器刚才打了个盹儿，请再试一次吧");
                }
            } else {
                if (!redisUtil.delKey(phone + ".modifypassword")) {
                    return setResultError("服务器刚才打了个盹儿，请再试一次吧");
                }
            }
            return setResultSuccess("验证码正确");
        }
        return setResultError("验证码错误或已过期");
    }
}
