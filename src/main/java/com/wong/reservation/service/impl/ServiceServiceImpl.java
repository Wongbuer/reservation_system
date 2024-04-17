package com.wong.reservation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Service;
import com.wong.reservation.service.ServiceService;
import com.wong.reservation.mapper.ServiceMapper;

/**
* @author Wongbuer
* @description 针对表【service】的数据库操作Service实现
* @createDate 2024-04-17 21:34:49
*/
@org.springframework.stereotype.Service
public class ServiceServiceImpl extends ServiceImpl<ServiceMapper, Service> implements ServiceService{

    @Override
    public Result<?> addService(Service service) {
        boolean isSaved;
        try {
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




