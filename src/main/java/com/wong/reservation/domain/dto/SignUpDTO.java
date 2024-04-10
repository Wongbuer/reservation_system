package com.wong.reservation.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

/**
 * @author Wongbuer
 */
@Data
@Tag(name = "用户注册信息")
public class SignUpDTO {
    @Schema(title = "用户名", name = "username", example = "wongbuer", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    @Schema(title = "密码", name = "password", example = "111111", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
    @Schema(title = "手机号", name = "phone", example = "18613248725", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;
}
