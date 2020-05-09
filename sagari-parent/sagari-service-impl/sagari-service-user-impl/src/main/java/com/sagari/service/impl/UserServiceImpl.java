package com.sagari.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.common.utils.MD5Util;
import com.sagari.common.utils.RegexUtils;
import com.sagari.dto.input.ModifyPasswordDTO;
import com.sagari.dto.input.ModifyUserDTO;
import com.sagari.dto.input.UserSignUpInputDTO;
import com.sagari.service.UserService;
import com.sagari.service.entity.*;
import com.sagari.service.feign.VCodeServiceFeign;
import com.sagari.service.mapper.BanMapper;
import com.sagari.service.mapper.SignInHistoryMapper;
import com.sagari.service.mapper.UserMapper;
import com.sagari.service.util.ClientInfo;
import com.xxl.sso.core.login.SsoTokenLoginHelper;
import com.xxl.sso.core.user.XxlSsoUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @author biubiupiu~
 */
@RestController
public class UserServiceImpl extends BaseApiService<JSONObject> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SignInHistoryMapper historyMapper;
    @Autowired
    private BanMapper banMapper;
    @Autowired
    private ClientInfo clientInfo;
    @Autowired
    private VCodeServiceFeign codeServiceFeign;
    @Autowired
    private HttpServletRequest request;
    @Value("${modify.username.duration}")
    private Integer durationDay;

    @Override
    public BaseResponse<JSONObject> signUp(@RequestBody @Valid UserSignUpInputDTO signUpDTO,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return setResultError(errorMsg);
        }
        String phone = signUpDTO.getPhone();
        String vCode = signUpDTO.getVerifyCode();
        BaseResponse<JSONObject> isCorrect = codeServiceFeign.verifyCode(phone, 1, vCode);
        if (!isCorrect.getCode().equals(200)) {
            return isCorrect;
        }
        signUpDTO.setPassword(MD5Util.md5(signUpDTO.getPassword()));
        User user = new User();
        BeanUtils.copyProperties(signUpDTO, user);
        user.setCreateTime(System.currentTimeMillis());
        user.setUpdateTime(user.getCreateTime());
        user.setArticleCount(0);
        user.setFollowCount(0);
        user.setFansCount(0);
        int result = userMapper.signUp(user);
        if (result > 0) {
            return setResultSuccess("用户注册成功");
        } else {
            return setResultError("用户注册失败");
        }
    }

    @Override
    public BaseResponse<JSONObject> signIn(String account, String password) {
        if (StringUtils.isBlank(account)) {
            return setResultError("用户名不能为空");
        }
        if (StringUtils.isBlank(password)) {
            return setResultError("密码不能为空");
        }
        User user;
        SignIn signIn = new SignIn();
        signIn.setTime(System.currentTimeMillis());
        clientInfo.getClientInfo(request, signIn);
        if (RegexUtils.checkMobile(account)) {
            user = userMapper.signInByPhone(account);
            signIn.setType("手机号码登录");
        } else if (RegexUtils.checkEmail(account)) {
            user = userMapper.signInByEmail(account);
            signIn.setType("邮箱登录");
        } else {
            user = userMapper.signInByUsername(account);
            signIn.setType("用户名登录");
        }
        if (MD5Util.verify(password, user.getPassword())) {
            signIn.setUserId(user.getId());
            historyMapper.insertRecord(signIn);
            return setResultSuccess((JSONObject) JSON.toJSON(user));
        }
        return setResultError("用户名或密码错误");
    }

    @Override
    public Boolean isExist(@RequestParam(name = "userId") Integer userId) {
        if (userId == null) {
            return false;
        }
        return userMapper.isExist(userId) > 0;
    }

    @Override
    public BaseResponse<JSONObject> getSimpleUser(Integer id) {
        if (id.equals(0)) {
            String sessionId = request.getHeader("xxl-sso-session-id");
            if (sessionId != null) {
                XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
                if (xxlUser != null) {
                    id = Integer.valueOf(xxlUser.getUserid());
                }
            }
        }
        User user = userMapper.getSimpleUser(id);
        if (user != null) {
            return setResultSuccess((JSONObject) JSON.toJSON(user));
        }
        return setResultError("未获取到用户信息");
    }

    @Override
    public BaseResponse<JSONObject> getSimpleUserByList(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return setResultSuccess();
        }
        List<User> userList = userMapper.getSimpleUserByList(ids);
        JSONObject result = new JSONObject();
        result.put("users", JSON.toJSON(userList));
        return setResultSuccess(result);
    }

    @Override
    public Boolean isExistByPhone(String phone) {
        return userMapper.isExistByPhone(phone) > 0;
    }

    @Override
    public Boolean isExistByUsername(String username) {
        return userMapper.isExistByUsername(username) > 0;
    }

    @Override
    public Boolean isExistByEmail(String email) {
        return userMapper.isExistByEmail(email) > 0;
    }

    @Override
    public Boolean incrementArticleCount(Integer id) {
        return userMapper.incrementArticleCount(id) > 0;
    }

    @Override
    public Boolean incrementFollowCount(Integer id) {
        return userMapper.incrementFollowCount(id) > 0;
    }

    @Override
    public Boolean decreaseFollowCount(Integer id) {
        return userMapper.decreaseFollowCount(id) > 0;
    }

    @Override
    public Boolean incrementFollowCountN(Integer id, Integer count) {
        return userMapper.incrementFollowCountN(id, count) > 0;
    }

    @Override
    public Boolean decreaseFollowCountN(Integer id, Integer count) {
        return userMapper.decreaseFollowCountN(id, count) > 0;
    }

    @Override
    public Boolean incrementFollowCountBatch(@RequestBody List<Integer> ids) {
        return userMapper.incrementFollowCountBatch(ids) > 0;
    }

    @Override
    public Boolean decreaseFollowCountBatch(@RequestBody List<Integer> ids) {
        return userMapper.decreaseFollowCountBatch(ids) > 0;
    }

    @Override
    public Boolean incrementFansCount(Integer id) {
        return userMapper.incrementFansCount(id) > 0;
    }

    @Override
    public Boolean decreaseFansCount(Integer id) {
        return userMapper.decreaseFansCount(id) > 0;
    }

    @Override
    public Boolean incrementFansCountN(Integer id, Integer count) {
        return userMapper.incrementFansCountN(id, count) > 0;
    }

    @Override
    public Boolean decreaseFansCountN(Integer id, Integer count) {
        return userMapper.decreaseFansCountN(id, count) > 0;
    }

    @Override
    public Boolean incrementFansCountBatch(@RequestBody List<Integer> ids) {
        return userMapper.incrementFansCountBatch(ids) > 0;
    }

    @Override
    public Boolean decreaseFansCountBatch(@RequestBody List<Integer> ids) {
        return userMapper.decreaseFansCountBatch(ids) > 0;
    }

    @Override
    public BaseResponse<JSONObject> getUserAll() {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        UserVO userVO = userMapper.getUserAll(userId);
        if (userVO != null) {
            return setResultSuccess((JSONObject) JSON.toJSON(userVO));
        }
        return setResultError("用户不存在");
    }

    @Override
    public BaseResponse<JSONObject> modifyUser(@RequestBody @Valid ModifyUserDTO modifyUserDTO,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return setResultError(errorMsg);
        }
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        User user = new User();
        BeanUtils.copyProperties(modifyUserDTO, user);
        user.setId(userId);
        user.setUpdateTime(System.currentTimeMillis());
        if (!StringUtils.isBlank(user.getGender())) {
            if (!"男".equals(user.getGender()) && !"女".equals(user.getGender())) {
                return setResultError("性别数据格式错误");
            }
        }
        if (!StringUtils.isBlank(user.getUsername()) && !user.getUsername().equals(xxlUser.getUsername())) {
            Long lastTime = userMapper.getLastRecord(userId);
            if (lastTime != null) {
                Long nowTime = System.currentTimeMillis();
                long duration = nowTime - lastTime;
                long minDuration = durationDay * 24 * 60 * 60 * 1000L;
                if (duration < minDuration) {
                    return setResultError("距离上次修改用户名不足" + durationDay + "天");
                }
            }
        }
        if (userMapper.modifyUser(user) > 0) {
            if (!StringUtils.isBlank(user.getUsername()) && !user.getUsername().equals(xxlUser.getUsername())) {
                UsernameRecord usernameRecord = new UsernameRecord();
                usernameRecord.setUserId(userId);
                usernameRecord.setOldUsername(xxlUser.getUsername());
                usernameRecord.setNewUsername(user.getUsername());
                usernameRecord.setCreateTime(System.currentTimeMillis());
                userMapper.insertUsernameRecord(usernameRecord);
            }
            return setResultSuccess("用户信息修改成功");
        }
        return setResultError("用户信息修改失败");
    }

    @Override
    public BaseResponse<JSONObject> modifyPassword(@RequestBody @Valid ModifyPasswordDTO modifyPasswordDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return setResultError(errorMsg);
        }
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        String phone = userMapper.getPhone(userId);
        String vcode = modifyPasswordDTO.getVerifyCode();
        BaseResponse<JSONObject> response = codeServiceFeign.verifyCode(phone, 2, vcode);
        if (!response.getCode().equals(200)) {
            return response;
        }
        String password = MD5Util.md5(modifyPasswordDTO.getPassword());
        if (userMapper.modifyPassword(userId, password, System.currentTimeMillis()) > 0) {
            return setResultSuccess("密码修改成功");
        }
        return setResultError("密码修改失败");
    }

    @Override
    public BaseResponse<JSONObject> getPhone() {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        String phone = userMapper.getPhone(userId);
        JSONObject result = new JSONObject();
        result.put("phone", phone);
        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> bindQQ(String account, String qqId) {
        if (RegexUtils.checkMobile(account)) {
            if (userMapper.bindQQByPhone(account, qqId) > 0) {
                return setResultSuccess();
            }
        }
        if (userMapper.bindQQByUsername(account, qqId) > 0) {
            return setResultSuccess();
        }
        return setResultError("bind failed");
    }

    @Override
    public BaseResponse<JSONObject> unbindQQ() {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (userMapper.unbindQQ(userId) > 0) {
            return setResultSuccess();
        }
        return setResultError("unbind qq failed");
    }

    @Override
    public BaseResponse<JSONObject> getHistory(Integer page, Integer size) {
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
        List<SignIn> history = historyMapper.getHistory(userId);
        PageInfo<SignIn> result = new PageInfo<>(history);

        return setResultSuccess((JSONObject) JSON.toJSON(result));
    }

    @Override
    public BaseResponse<JSONObject> modifyAvatar(String avatar) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (RegexUtils.checkURL(avatar)) {
            if (userMapper.modifyAvatar(userId, avatar) > 0) {
                return setResultSuccess();
            }
            return setResultError("modify avatar failed");
        }
        return setResultError("the avatar must be a URL");
    }

    @Override
    public BaseResponse<JSONObject> getBanRecord(Integer page, Integer size) {
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
        List<Ban> records = banMapper.getBanRecord(userId);
        PageInfo<Ban> pageInfo = new PageInfo<>(records);
        return setResultSuccess((JSONObject) JSON.toJSON(pageInfo));
    }

    @Override
    public BaseResponse<JSONObject> modifyEmail(String oldEmail, String newEmail) {
        if (!RegexUtils.checkEmail(oldEmail)) {
            return setResultError("the old email address format isn't incorrect");
        }
        if (!RegexUtils.checkEmail(newEmail)) {
            return setResultError("the new email address format isn't incorrect");
        }
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (userMapper.modifyEmail(userId, oldEmail, newEmail) > 0) {
            return setResultSuccess();
        }
        return setResultError("modify email failed,may be the old email is incorrect");
    }

    @Override
    public BaseResponse<JSONObject> getOtherPlatform() {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        JSONObject result = new JSONObject();
        User user = userMapper.getOtherPlatform(userId);
	    System.out.println(user);
        if (StringUtils.isNotBlank(user.getQqId())) {
            result.put("qqId", user.getQqId());
        }
        if (StringUtils.isNotBlank(user.getBaiduId())) {
            result.put("baiduId", user.getBaiduId());
        }
        if (StringUtils.isNotBlank(user.getGithubId())) {
            result.put("githubId",user.getGithubId());
        }
        return setResultSuccess(result);
    }
}
