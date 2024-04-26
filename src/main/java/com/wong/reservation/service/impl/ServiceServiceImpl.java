package com.wong.reservation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Service;
import com.wong.reservation.mapper.ServiceMapper;
import com.wong.reservation.service.ServiceService;
import jakarta.annotation.Resource;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author Wongbuer
* @description 针对表【service】的数据库操作Service实现
* @createDate 2024-04-17 21:34:49
*/
@org.springframework.stereotype.Service
public class ServiceServiceImpl extends ServiceImpl<ServiceMapper, Service> implements ServiceService{

    @Resource
    private FileStorageService fileStorageService;

    @Override
    public Result<?> addService(Service service, MultipartFile icon) {
        boolean isSaved;
        try {
            FileInfo fileInfo = fileStorageService.of(icon).upload();
            if (fileInfo != null) {
                service.setIconUrl(fileInfo.getUrl());
            }
            isSaved = save(service);
        } catch (Exception e) {
            return Result.fail("添加服务类型失败");
        }
        return isSaved ? Result.success("添加服务类型成功") : Result.fail("添加服务类型失败");
    }

    @Override
    public Result<?> updateService(Service service) {
        boolean isUpdated;
        try {
            isUpdated = updateById(service);
        } catch (Exception e) {
            return Result.fail("修改服务类型失败");
        }
        return isUpdated ? Result.success("修改服务类型成功") : Result.fail("修改服务类型失败");
    }
}




