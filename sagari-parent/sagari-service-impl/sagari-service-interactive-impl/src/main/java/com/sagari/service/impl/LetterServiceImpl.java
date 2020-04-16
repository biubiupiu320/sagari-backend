package com.sagari.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.service.LetterService;
import com.sagari.service.entity.Letter;
import com.sagari.service.entity.Person;
import com.sagari.service.feign.UserServiceFeign;
import com.sagari.service.mapper.LetterMapper;
import com.xxl.sso.core.login.SsoTokenLoginHelper;
import com.xxl.sso.core.user.XxlSsoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class LetterServiceImpl extends BaseApiService<JSONObject> implements LetterService {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserServiceFeign userServiceFeign;
    @Autowired
    private LetterMapper letterMapper;

    @Override
    public BaseResponse<JSONObject> getPersonList(Integer page, Integer size) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (page < 1) {
            page = 1;
        }
        if (size < 10) {
            size = 10;
        }
        PageHelper.startPage(page, size);
        List<Person> list = letterMapper.getPersonList(userId);
        if (list == null || list.isEmpty()) {
            return setResultSuccess();
        }
        List<Integer> toIds = list.stream().map(Person::getToId).collect(Collectors.toList());
        JSONArray users = userServiceFeign.getSimpleUserByList(toIds).getData().getJSONArray("users");
        Map<Integer, JSONObject> userMap = users.stream()
                .map(o -> (JSONObject) JSON.toJSON(o))
                .collect(Collectors.toMap(o -> o.getInteger("id"), o -> o));
        for (Person person : list) {
            JSONObject user = userMap.get(person.getToId());
            person.setToUsername(user.getString("username"));
            person.setToAvatar(user.getString("avatar"));
        }
        PageInfo<Person> pageInfo = new PageInfo<>(list);
        return setResultSuccess((JSONObject) JSON.toJSON(pageInfo));
    }

    @Override
    public BaseResponse<JSONObject> getLetters(Integer toId, Integer page, Integer size) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (page < 1) {
            page = 1;
        }
        if (size < 10) {
            size = 10;
        }
        PageHelper.startPage(page, size);
        List<Letter> list = letterMapper.getLetters(userId, toId);
        PageInfo<Letter> pageInfo = new PageInfo<>(list);
        return setResultSuccess((JSONObject) JSON.toJSON(pageInfo));
    }
}
