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
import java.util.List;

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
    @GetMapping("/sign-in")
    public BaseResponse<JSONObject> signIn(@RequestParam(name = "account") String account,
                                           @RequestParam(name = "password") String password);

    @ApiOperation(value = "判断用户是否存在接口")
    @GetMapping("/isExist")
    public Boolean isExist(@RequestParam(name = "userId") Integer userId);

    @ApiOperation(value = "根据用户ID获取用户的简易信息，包括用户ID，用户名，用户头像地址，文章数量，粉丝数量")
    @GetMapping("/getSimpleUser")
    public BaseResponse<JSONObject> getSimpleUser(@RequestParam(name = "id") Integer id);

    @ApiOperation(value = "根据用户ID列表获取用户的简易信息，包括用户ID，用户名，用户头像地址，文章数量，粉丝数量")
    @PostMapping("/getSimpleUserByList")
    public BaseResponse<JSONObject> getSimpleUserByList(@RequestBody List<Integer> ids);

    @ApiOperation(value = "根据用户绑定的手机号判断用户是否存在")
    @GetMapping("/isExistByPhone")
    public Boolean isExistByPhone(@RequestParam(name = "phone") String phone);

    @ApiOperation(value = "根据用户绑定的手机号判断用户是否存在")
    @GetMapping("/isExistByUsername")
    public Boolean isExistByUsername(@RequestParam(name = "username") String username);

    @ApiOperation(value = "根据用户绑定的手机号判断用户是否存在")
    @GetMapping("/isExistByEmail")
    public Boolean isExistByEmail(@RequestParam(name = "email") String email);

    @ApiOperation(value = "增加用户发表的文章数量")
    @GetMapping("/incrementArticleCount")
    public Boolean incrementArticleCount(@RequestParam(name = "id") Integer id);
}
