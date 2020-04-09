package com.sagari.dto.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author biubiupiu~
 */
public class ModifyPasswordDTO {

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 16, message = "密码最少为6个字符，最多为16个字符")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[._~!@#$^&*])[A-Za-z0-9._~!@#$^&*%]{6,16}$",
            message = "密码中包含非法字符")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String verifyCode;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ModifyPasswordDTO{");
        sb.append("password='").append(password).append('\'');
        sb.append(", verifyCode='").append(verifyCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
