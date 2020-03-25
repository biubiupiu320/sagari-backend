package com.xxl.sso.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import com.sagari.common.constants.Constants;
import com.xxl.sso.core.login.SsoTokenLoginHelper;
import com.xxl.sso.core.store.SsoLoginStore;
import com.xxl.sso.core.store.SsoSessionIdHelper;
import com.xxl.sso.core.user.XxlSsoUser;
import com.xxl.sso.server.feign.UserServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * sso server (for app)
 *
 * @author xuxueli 2018-04-08 21:02:54
 */
@RestController
public class AppController{

    @Autowired
    private UserServiceFeign userServiceFeign;
    @Autowired
    private HttpServletRequest request;

    /**
     * Login
     *
     * @param account
     * @param password
     * @param isRem
     * @return
     */
    @GetMapping("/login")
    public BaseResponse<String> login(@RequestParam(name = "account") String account,
                                      @RequestParam(name = "password") String password,
                                      @RequestParam(name = "isRem", defaultValue = "false") Boolean isRem) {
        // valid login
        BaseResponse<JSONObject> result = userServiceFeign.signIn(account, password);
        if (!result.getCode().equals(Constants.HTTP_RES_CODE_200)) {
            return new BaseResponse<>(Constants.HTTP_RES_CODE_500, result.getMsg(), null);
        }

        // 1、make xxl-sso user
        XxlSsoUser xxlUser = new XxlSsoUser();
        xxlUser.setUserid(String.valueOf(result.getData().get("id")));
        xxlUser.setUsername(result.getData().getString("username"));
        xxlUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
        if (isRem) {
            SsoLoginStore.setRedisExpireMinite(14400);
            xxlUser.setExpireMinite(14400);
        } else {
            xxlUser.setExpireMinite(SsoLoginStore.getRedisExpireMinite());
        }

        xxlUser.setExpireFreshTime(System.currentTimeMillis());

        // 2、generate sessionId + storeKey
        String sessionId = SsoSessionIdHelper.makeSessionId(xxlUser);

        // 3、login, store storeKey
        SsoTokenLoginHelper.login(sessionId, xxlUser);

        // 4、return sessionId
        return new BaseResponse<>(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, sessionId);
    }

    /**
     * Logout
     * @return
     */
    @GetMapping("/logout")
    public BaseResponse<JSONObject> logout() {
        String sessionId = request.getHeader("xxl-sso-session-id");
        SsoTokenLoginHelper.logout(sessionId);
        return new BaseResponse<>(Constants.HTTP_RES_CODE_200, "用户已退出", null);
    }

    /**
     * loginCheck
     * @return
     */
    @GetMapping("/loginCheck")
    public BaseResponse<String> loginCheck() {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return new BaseResponse<>(Constants.HTTP_RES_CODE_500, "用户未登录", null);
        }
        BaseResponse<JSONObject> user = userServiceFeign.getSimpleUser(Integer.valueOf(xxlUser.getUserid()));
        return new BaseResponse<>(Constants.HTTP_RES_CODE_200,
                Constants.HTTP_RES_CODE_200_VALUE,
                user.getData().toJSONString());
    }

}