package com.wong.reservation.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.dto.UploadDTO;
import jakarta.annotation.Resource;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wongbuer
 * @createDate 2024/5/5
 */
@SaIgnore
@RestController
@RequestMapping(value = "/upload", method = {RequestMethod.GET, RequestMethod.POST})
public class UploadController {
    @Resource
    private FileStorageService fileStorageService;

    @RequestMapping
    public Result<?> upload(@ModelAttribute UploadDTO uploadDTO) {
        System.out.println(uploadDTO.getId());
        System.out.println(uploadDTO.getFile());
        FileInfo fileInfo = fileStorageService.of(uploadDTO.getFile()).upload();
        if (fileInfo == null) {
            return Result.fail("上传失败");
        } else {
            return Result.success("上传成功", fileInfo.getUrl());
        }
    }
}
