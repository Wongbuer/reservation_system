package com.wong.reservation.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Wongbuer
 * @createDate 2024/4/17
 */
@Data
public class EmployeeServiceDTO {
    @Schema(title = "员工服务id", name = "id", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long id;
    @Schema(title = "员工id", name = "employeeId", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long employeeId;
    @Schema(title = "服务id", name = "serviceId", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long serviceId;
    @Schema(title = "服务价格", name = "price", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;
    @Schema(title = "服务时间单位", name = "timeUnit", example = "hour", requiredMode = Schema.RequiredMode.REQUIRED)
    private String timeUnit;
    @Schema(title = "服务介绍", name = "description", example = "这是一个服务介绍", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;
    @Schema(title = "服务详细介绍图片", name = "detailedPictures", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<MultipartFile> detailedPictures;
    @Schema(title = "服务详细介绍图片(URL)", name = "detailedPicturesUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<String> detailedPicturesUrl;
}
