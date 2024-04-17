package com.wong.reservation.service;

import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Service;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Wongbuer
 * @description 针对表【service】的数据库操作Service
 * @createDate 2024-04-17 21:34:49
 */
public interface ServiceService extends IService<Service> {
    /**
     * 添加服务类型
     *
     * @param service 服务类型
     * @return Result<?> 添加结果
     */
    Result<?> addService(Service service);

    /**
     * 修改服务类型
     *
     * @param service 服务类型
     * @return Result<?> 修改结果
     */
    Result<?> updateService(Service service);
}
