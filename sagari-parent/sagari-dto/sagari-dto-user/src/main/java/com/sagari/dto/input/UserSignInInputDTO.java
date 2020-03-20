package com.sagari.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author biubiupiu~
 */
@Data
@ApiModel(value = "用户登录实体类")
public class UserSignInInputDTO {

    @ApiModelProperty(value = "用户名/手机号/邮箱")
    @NotBlank(message = "用户名不能为空")
    private String account;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
