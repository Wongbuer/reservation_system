package com.wong.reservation.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wong.reservation.domain.dto.EmployeeServiceDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Employee;
import com.wong.reservation.mapper.EmployeeMapper;
import com.wong.reservation.service.EmployeeService;
import com.wong.reservation.service.EmployeeServiceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Wongbuer
* @description 针对表【employee】的数据库操作Service实现
* @createDate 2024-04-17 16:47:48
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{
    @Resource
    private EmployeeServiceService employeeServiceService;
    @Override
    public Result<?> bindingEmployeeInfo(Employee employee) {
        // 从登录信息中获取userId
        long userId = StpUtil.getLoginIdAsLong();
        // 判断该userId是否已绑定employee
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUserId, userId);
        if (count(wrapper) > 0) {
            return Result.fail(40000, "该用户已绑定员工");
        }
        // 添加员工
        employee.setUserId(userId);
        return save(employee) ? Result.success("添加员工成功") : Result.fail(40000, "添加员工失败");
    }

    @Override
    public Result<?> publishService(EmployeeServiceDTO employeeServiceDTO) {
        long employeeId = getEmployeeId();
        employeeServiceDTO.setEmployeeId(employeeId);
        // 添加员工服务
        return employeeServiceService.addEmployeeService(employeeServiceDTO) ? Result.success("添加员工服务成功") : Result.fail(40000, "添加员工服务失败");
    }

    @Override
    public Result<?> deleteService(Long employeeServiceId) {
        Assert.notNull(employeeServiceId);
        long employeeId = getEmployeeId();
        return employeeServiceService.deleteService(employeeId, employeeServiceId) ? Result.success("删除员工服务成功") : Result.fail(40000, "删除员工服务失败");
    }

    @Override
    public Result<List<com.wong.reservation.domain.entity.EmployeeService>> getServiceList() {
        long employeeId = getEmployeeId();
        LambdaQueryWrapper<com.wong.reservation.domain.entity.EmployeeService> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(com.wong.reservation.domain.entity.EmployeeService::getEmployeeId, employeeId);
        return Result.success(employeeServiceService.list(wrapper));
    }


    private long getEmployeeId() {
        // 获取用户id
        long userId = StpUtil.getLoginIdAsLong();
        // 查找对应employeeId并设置
        Employee employee = getOne(new LambdaQueryWrapper<Employee>().eq(Employee::getUserId, userId));
        return employee.getId();
    }
}




