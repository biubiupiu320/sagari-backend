package com.sagari.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.common.utils.RegexUtils;
import com.sagari.service.entity.SignIn;
import com.sagari.service.entity.UserVO;
import com.sagari.service.mapper.SignInHistoryMapper;
import com.sagari.service.mapper.UserMapper;
import com.sagari.service.util.ClientInfo;
import com.xxl.sso.core.login.SsoTokenLoginHelper;
import com.xxl.sso.core.store.SsoLoginStore;
import com.xxl.sso.core.store.SsoSessionIdHelper;
import com.xxl.sso.core.user.XxlSsoUser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * @author biubiupiu~
 */
@RestController
public class QQController extends BaseApiService<JSONObject> {

    @Value("${qq.app.id}")
    private String appId;
    @Value("${qq.app.key}")
    private String appKey;
    @Value("${qq.redirect-uri}")
    private String redirectUri;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SignInHistoryMapper historyMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ClientInfo clientInfo;
    private Logger log = LoggerFactory.getLogger(QQController.class);

    @GetMapping("/qq_signin")
    public BaseResponse<JSONObject> qqLogin(@RequestParam("code") String code) {
        String accessToken = getAccessToken(code);
        String openId = getOpenId(accessToken);
        UserVO user = userMapper.getUserByQQ(openId);
        if (user == null) {
            JSONObject result = getUserInfo(accessToken, openId);
            result.put("open_id", openId);
            return setResultError("this qq account not bind any user", result);
        }
        XxlSsoUser xxlSsoUser = new XxlSsoUser();
        xxlSsoUser.setUserid(String.valueOf(user.getId()));
        xxlSsoUser.setUsername(user.getUsername());
        xxlSsoUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
        SsoLoginStore.setRedisExpireMinite(14400);
        xxlSsoUser.setExpireMinite(14400);
        xxlSsoUser.setExpireFreshTime(System.currentTimeMillis());
        String sessionId = SsoSessionIdHelper.makeSessionId(xxlSsoUser);
        SsoTokenLoginHelper.login(sessionId, xxlSsoUser);
        SignIn signIn = new SignIn();
        signIn.setUserId(user.getId());
        signIn.setTime(System.currentTimeMillis());
        signIn.setType("QQ登录");
        clientInfo.getClientInfo(request, signIn);
        historyMapper.insertRecord(signIn);
        JSONObject result = new JSONObject();
        result.put("sessionId", sessionId);
        return setResultSuccess("qq login success", result);
    }

    private String getAccessToken(String code) {
        StringBuffer sb = new StringBuffer("grant_type=authorization_code&");
        sb.append("client_id=").append(appId).append("&");
        sb.append("client_secret=").append(appKey).append("&");
        sb.append("code=").append(code).append("&");
        sb.append("redirect_uri=").append(redirectUri);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graph.qq.com/oauth2.0/token?" + sb.toString())
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = Objects.requireNonNull(response.body()).string();
            String[] split = string.split("&");
            for (String s : split) {
                int index = s.indexOf("access_token");
                if (index != -1) {
                    index += 13;
                    return s.substring(index);
                }
            }
            response.close();
            return "";
        } catch (IOException e) {
            log.error("get qq access_token failed");
            return "";
        }
    }

    private String getOpenId(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graph.qq.com/oauth2.0/me?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = Objects.requireNonNull(response.body()).string();
            String[] split = string.split(",");
            for (String s : split) {
                int index = s.indexOf("openid");
                if (index != -1) {
                    index += 9;
                    int lastIndex = s.lastIndexOf("\"");
                    return s.substring(index, lastIndex);
                }
            }
            response.close();
            return "";
        } catch (IOException e) {
            log.error("get qq openid failed");
            return "";
        }
    }

    private JSONObject getUserInfo(String accessToken, String openId) {
        OkHttpClient client = new OkHttpClient();
        StringBuffer sb = new StringBuffer("access_token=");
        sb.append(accessToken).append("&");
        sb.append("oauth_consumer_key=").append(appId).append("&");
        sb.append("openid=").append(openId);
        Request request = new Request.Builder()
                .url("https://graph.qq.com/user/get_user_info?" + sb.toString())
                .build();
        try {
            Response response = client.newCall(request).execute();
            return JSON.parseObject(Objects.requireNonNull(response.body()).string());
        } catch (IOException e) {
            log.error("get qq user information failed");
            return new JSONObject();
        }
    }

}
