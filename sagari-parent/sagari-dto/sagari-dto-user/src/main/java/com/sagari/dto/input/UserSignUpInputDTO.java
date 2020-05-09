package com.sagari.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 * @author biubiupiu~
 */
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
    @Pattern(regexp = "0?(13|14|15|18|17|19)[0-9]{9}", message = "无效的手机号码")
    private String phone;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;

    @ApiModelProperty(value = "用户头像链接")
    @NotBlank(message = "用户头像链接不能为空")
    @Pattern(regexp = "^((https|http)?:\\/\\/)[^\\s]+", message = "无效的用户头像")
    private String avatar;

    private String qqId;
    private String baiduId;
    private String githubId;
    private String gender;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getQqId() {
        return qqId;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId;
    }

    public String getBaiduId() {
        return baiduId;
    }

    public void setBaiduId(String baiduId) {
        this.baiduId = baiduId;
    }

    public String getGithubId() {
        return githubId;
    }

    public void setGithubId(String githubId) {
        this.githubId = githubId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserSignUpInputDTO{");
        sb.append("username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", verifyCode='").append(verifyCode).append('\'');
        sb.append(", avatar='").append(avatar).append('\'');
        sb.append(", qqId='").append(qqId).append('\'');
        sb.append(", baiduId='").append(baiduId).append('\'');
        sb.append(", githubId='").append(githubId).append('\'');
        sb.append(", gender='").append(gender).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
