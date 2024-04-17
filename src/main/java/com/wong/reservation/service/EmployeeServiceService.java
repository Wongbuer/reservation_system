package com.wong.reservation.service;

import com.wong.reservation.domain.dto.EmployeeServiceDTO;
import com.wong.reservation.domain.entity.EmployeeService;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Wongbuer
* @description 针对表【employee_service】的数据库操作Service
* @createDate 2024-04-17 17:18:21
*/
public interface EmployeeServiceService extends IService<EmployeeService> {

    boolean addEmployeeService(EmployeeServiceDTO employeeServiceDTO);

    boolean deleteService(long employeeId, Long employeeServiceId);
}
