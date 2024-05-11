package com.wong.reservation.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Wongbuer
 * @createDate 2024/5/5
 */
@Data
public class UploadDTO {
    @Schema(title = "员工id", name = "employeeId", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
    @Schema(title = "文件", name = "file", requiredMode = Schema.RequiredMode.REQUIRED)
    private MultipartFile file;
}
