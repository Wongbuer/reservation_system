package com.wong.reservation.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Wongbuer
 */
@Data
public class LoginDTO {
    @Schema(title = "账号", name = "account", example = "wongbuer", requiredMode = Schema.RequiredMode.REQUIRED)
    private String account;
    @Schema(title = "密码", name = "password", example = "111111", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
    @Schema(title = "验证码", name = "captchaDTO", type = "object", example = "{\"code\":\"123\",\"uuid\":\"123\"}", requiredMode = Schema.RequiredMode.REQUIRED)
    private CaptchaDTO captchaDTO;
    @Schema(title = "登录方式", name = "type", example = "sms", allowableValues = {"sms", "password"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;
}
