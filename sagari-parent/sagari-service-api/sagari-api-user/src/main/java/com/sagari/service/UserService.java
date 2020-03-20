package com.sagari.service;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.UserSignInInputDTO;
import com.sagari.dto.input.UserSignUpInputDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author biubiupiu~
 */
@Api(tags = "用户服务接口")
public interface UserService {

    @ApiOperation(value = "注册接口")
    @PostMapping("/sign-up")
    public BaseResponse<JSONObject> signUp(@RequestBody @Valid UserSignUpInputDTO signUpDTO,
                                           BindingResult bindingResult);
    @ApiOperation(value = "登录接口")
    @PostMapping("/sign-in")
    public BaseResponse<JSONObject> signIn(@RequestBody @Valid UserSignInInputDTO signInDTO,
                                           BindingResult bindingResult, HttpServletRequest request);

    @ApiOperation(value = "判断用户是否存在接口")
    @GetMapping("/isExist")
    public Boolean isExist(@RequestParam(name = "userId") Integer userId);
}
