package com.wong.reservation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wong.reservation.domain.dto.EmployeeServiceDTO;
import com.wong.reservation.domain.entity.EmployeeService;
import com.wong.reservation.service.EmployeeServiceService;
import com.wong.reservation.mapper.EmployeeServiceMapper;
import org.springframework.stereotype.Service;

/**
* @author Wongbuer
* @description 针对表【employee_service】的数据库操作Service实现
* @createDate 2024-04-17 17:18:21
*/
@Service
public class EmployeeServiceServiceImpl extends ServiceImpl<EmployeeServiceMapper, EmployeeService> implements EmployeeServiceService{

    @Override
    public boolean addEmployeeService(EmployeeServiceDTO employeeServiceDTO) {
        EmployeeService employeeService = new EmployeeService();
        employeeService.setEmployeeId(employeeServiceDTO.getEmployeeId());
        employeeService.setServiceId(employeeServiceDTO.getServiceId());
        employeeService.setPrice(employeeServiceDTO.getPrice());
        employeeService.setTimeUint(employeeServiceDTO.getTimeUint());

        // 查询有无重复数据
        LambdaQueryWrapper<EmployeeService> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmployeeService::getEmployeeId, employeeServiceDTO.getEmployeeId())
                .eq(EmployeeService::getServiceId, employeeServiceDTO.getServiceId());
        if (count(wrapper) > 0) {
            return false;
        }
        boolean isSaved;
        try {
            isSaved = save(employeeService);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return isSaved;
    }

    @Override
    public boolean deleteService(long employeeId, Long employeeServiceId) {
        LambdaQueryWrapper<EmployeeService> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmployeeService::getEmployeeId, employeeId)
                .eq(EmployeeService::getId, employeeServiceId);
        boolean isRemoved;
        try {
            isRemoved = remove(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return isRemoved;
    }
}




