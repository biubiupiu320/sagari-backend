package com.sagari.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.common.utils.MD5Util;
import com.sagari.common.utils.RegexUtils;
import com.sagari.dto.input.UserSignInInputDTO;
import com.sagari.dto.input.UserSignUpInputDTO;
import com.sagari.service.UserService;
import com.sagari.service.entity.SignIn;
import com.sagari.service.entity.User;
import com.sagari.service.feign.VCodeServiceFeign;
import com.sagari.service.mapper.SignInHistoryMapper;
import com.sagari.service.mapper.UserMapper;
import com.sagari.service.util.ClientInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
    private ClientInfo clientInfo;
    @Autowired
    private VCodeServiceFeign codeServiceFeign;

    @Override
    public BaseResponse<JSONObject> signUp(@RequestBody @Valid UserSignUpInputDTO signUpDTO,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return setResultError(errorMsg);
        }
        String phone = signUpDTO.getPhone();
        String type = "1";
        String vCode = signUpDTO.getVerifyCode();
        BaseResponse<JSONObject> isCorrect = codeServiceFeign.verifyCode(phone, type, vCode);
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
    public BaseResponse<JSONObject> signIn(@RequestBody @Valid UserSignInInputDTO signInDTO,
                                           BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return setResultError(errorMsg);
        }
        String account = signInDTO.getAccount();
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
        if (MD5Util.verify(signInDTO.getPassword(), user.getPassword())) {
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
}
