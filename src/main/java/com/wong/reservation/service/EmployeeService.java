package com.wong.reservation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wong.reservation.domain.dto.EmployeeServiceDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Employee;

import java.util.List;

/**
 * @author Wongbuer
 * @description 针对表【employee】的数据库操作Service
 * @createDate 2024-04-17 16:47:48
 */
public interface EmployeeService extends IService<Employee> {

    /**
     * 用户绑定员工信息
     *
     * @param employee 员工信息
     * @return 添加结果
     */
    Result<?> bindingEmployeeInfo(Employee employee);

    /**
     * 发布服务(服务必须是已有的)
     *
     * @param employeeServiceDTO 员工服务信息类
     * @return Result<?> 发布结果
     */
    Result<?> publishService(EmployeeServiceDTO employeeServiceDTO);

    /**
     * 删除服务
     *
     * @param employeeServiceId 员工id
     * @return Result<?> 删除结果
     */
    Result<?> deleteService(Long employeeServiceId);

    /**
     * 获取当前用户对应员工的服务列表
     *
     * @return Result<List<com.wong.reservation.domain.entity.EmployeeService>> 服务列表
     */
    Result<List<com.wong.reservation.domain.entity.EmployeeService>> getServiceList();
}
