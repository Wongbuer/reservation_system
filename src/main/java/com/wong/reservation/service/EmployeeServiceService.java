package com.wong.reservation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wong.reservation.domain.vo.RecommendServiceVO;
import com.wong.reservation.domain.dto.EmployeeServiceDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.EmployeeService;

import java.util.List;

/**
* @author Wongbuer
* @description 针对表【employee_service】的数据库操作Service
* @createDate 2024-04-17 17:18:21
*/
public interface EmployeeServiceService extends IService<EmployeeService> {

    /**
     * 删除员工服务
     *
     * @param employeeServiceId 员工服务id
     * @return Result<?> 删除结果
     */
    Result<?> deleteService(Long employeeServiceId);

    /**
     * 获取推荐服务
     *
     * @return Result<List<EmployeeService>>
     */
    Result<List<RecommendServiceVO>> getRecommendServiceList();

    /**
     * 发布服务
     *
     * @param employeeServiceDTO 员工任务DTO
     * @return 发布结果
     */
    Result<?> publishService(EmployeeServiceDTO employeeServiceDTO);

    /**
     * 获取当前用户对应员工的服务列表
     *
     * @return Result<List<com.wong.reservation.domain.entity.EmployeeService>> 服务列表
     */
    Result<List<EmployeeService>> getServiceList();
}
