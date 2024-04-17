package com.wong.reservation.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Wongbuer
 * @createDate 2024/4/13
 */
@Data
public class AddressDTO {
    @Schema(title = "收货人ID", name = "id", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long id;
    @Schema(title = "用户ID", name = "userId", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;
    @Schema(title = "收货人姓名", name = "name", example = "张三", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(title = "收货人电话", name = "phone", example = "12345678901", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;
    @Schema(title = "收货人省份", name = "province", example = "广东省", requiredMode = Schema.RequiredMode.REQUIRED)
    private String province;
    @Schema(title = "收货人城市", name = "city", example = "广州市", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;
    @Schema(title = "收货人区县", name = "district", example = "天河区", requiredMode = Schema.RequiredMode.REQUIRED)
    private String district;
    @Schema(title = "收货人详细地址", name = "fullAddress", example = "天河区天河路1号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fullAddress;
    @Schema(title = "收货人邮政编码", name = "postalCode", example = "123456", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String postalCode;
}
