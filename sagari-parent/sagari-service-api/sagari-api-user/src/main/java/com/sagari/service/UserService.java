package com.sagari.service;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.ModifyPasswordDTO;
import com.sagari.dto.input.ModifyUserDTO;
import com.sagari.dto.input.UserSignUpInputDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    @ApiOperation(value = "用户登录密码校验接口")
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

    @ApiOperation(value = "用户的关注数量+1")
    @GetMapping("/incrementFollowCount")
    public Boolean incrementFollowCount(@RequestParam(name = "id") Integer id);

    @ApiOperation(value = "用户的关注数量-1")
    @GetMapping("/decreaseFollowCount")
    public Boolean decreaseFollowCount(@RequestParam(name = "id") Integer id);

    @ApiOperation(value = "用户的关注数量+N")
    @GetMapping("/incrementFollowCountN")
    public Boolean incrementFollowCountN(@RequestParam(name = "id") Integer id,
                                         @RequestParam(name = "count") Integer count);

    @ApiOperation(value = "用户的关注数量-N")
    @GetMapping("/decreaseFollowCountN")
    public Boolean decreaseFollowCountN(@RequestParam(name = "id") Integer id,
                                        @RequestParam(name = "count") Integer count);

    @ApiOperation(value = "多名用户的关注数量+1")
    @PostMapping("/incrementFollowCountBatch")
    public Boolean incrementFollowCountBatch(@RequestBody List<Integer> ids);

    @ApiOperation(value = "多名用户的关注数量-1")
    @PostMapping("/decreaseFollowCountBatch")
    public Boolean decreaseFollowCountBatch(@RequestBody List<Integer> ids);

    @ApiOperation(value = "用户的粉丝数量+1")
    @GetMapping("/incrementFansCount")
    public Boolean incrementFansCount(@RequestParam(name = "id") Integer id);

    @ApiOperation(value = "用户的粉丝数量-1")
    @GetMapping("/decreaseFansCount")
    public Boolean decreaseFansCount(@RequestParam(name = "id") Integer id);

    @ApiOperation(value = "用户的粉丝数量+N")
    @GetMapping("/incrementFansCountN")
    public Boolean incrementFansCountN(@RequestParam(name = "id") Integer id,
                                       @RequestParam(name = "count") Integer count);

    @ApiOperation(value = "用户的粉丝数量-N")
    @GetMapping("/decreaseFansCountN")
    public Boolean decreaseFansCountN(@RequestParam(name = "id") Integer id,
                                      @RequestParam(name = "count") Integer count);

    @ApiOperation(value = "多名用户的粉丝数量+1")
    @PostMapping("/incrementFansCountBatch")
    public Boolean incrementFansCountBatch(@RequestBody List<Integer> ids);

    @ApiOperation(value = "多名用户的粉丝数量-1")
    @PostMapping("/decreaseFansCountBatch")
    public Boolean decreaseFansCountBatch(@RequestBody List<Integer> ids);

    @ApiOperation(value = "获取用户的所有信息")
    @GetMapping("/getUserAll")
    public BaseResponse<JSONObject> getUserAll();

    @ApiOperation(value = "修改用户信息")
    @PostMapping("/modifyUser")
    public BaseResponse<JSONObject> modifyUser(@RequestBody @Valid ModifyUserDTO modifyUserDTO,
                                               BindingResult bindingResult);

    @ApiOperation(value = "修改密码")
    @PostMapping("/modifyPassword")
    public BaseResponse<JSONObject> modifyPassword(@RequestBody @Valid ModifyPasswordDTO modifyPasswordDTO,
                                                   BindingResult bindingResult);

    @ApiOperation(value = "获取用户手机号码")
    @GetMapping("/getPhone")
    public BaseResponse<JSONObject> getPhone();

    @ApiOperation(value = "绑定QQ账号")
    @GetMapping("/bindQQ")
    public BaseResponse<JSONObject> bindQQ(@RequestParam(name = "account") String account,
                                           @RequestParam(name = "qqId") String qqId);

    @ApiOperation(value = "获取登录记录")
    @GetMapping("/getSignInHistory")
    public BaseResponse<JSONObject> getHistory(@RequestParam(name = "page") Integer page,
                                               @RequestParam(name = "size") Integer size);

    @ApiOperation(value = "修改用户头像")
    @GetMapping("/modifyAvatar")
    public BaseResponse<JSONObject> modifyAvatar(@RequestParam(name = "avatar") String avatar);

}
