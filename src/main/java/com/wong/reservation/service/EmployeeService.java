package com.wong.reservation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Employee;

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
     * 获取当前用户对应员工的id
     *
     * @return Long 员工id
     */
    Long getEmployeeId();
}
