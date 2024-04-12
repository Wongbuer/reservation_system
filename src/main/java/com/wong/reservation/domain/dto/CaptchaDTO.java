package com.wong.reservation.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Wongbuer
 * @createDate 2024/4/11
 */
@Data
public class CaptchaDTO {
    @Schema(title = "验证码", name = "code", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
    @Schema(title = "验证码唯一标识", name = "uuid", example = "123", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String uuid;
}
