package com.sagari.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 * @author biubiupiu~
 */
@Data
@ApiModel(value = "用户注册实体类")
public class UserSignUpInputDTO {

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Size(max = 10, message = "用户名不能超过10个字符")
    private String username;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 16, message = "密码最少为6个字符，最多为16个字符")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[._~!@#$^&*])[A-Za-z0-9._~!@#$^&*%]{6,16}$",
            message = "密码中包含非法字符")
    private String password;

    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}",
            message = "邮箱格式错误")
    private String email;

    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "0?(13|14|15|18|17)[0-9]{9}", message = "无效的手机号码")
    private String phone;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;

    @ApiModelProperty(value = "用户头像链接")
    @NotBlank(message = "用户头像链接不能为空")
    @Pattern(regexp = "^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+", message = "无效的用户头像")
    private String avatar;
}
