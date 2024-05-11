package com.wong.reservation.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Employee;
import com.wong.reservation.mapper.EmployeeMapper;
import com.wong.reservation.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
* @author Wongbuer
* @description 针对表【employee】的数据库操作Service实现
* @createDate 2024-04-17 16:47:48
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{
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
    public Long getEmployeeId() {
        // 获取用户id
        long userId = StpUtil.getLoginIdAsLong();
        // 查找对应employeeId并设置
        Employee employee = getOne(new LambdaQueryWrapper<Employee>().eq(Employee::getUserId, userId));
        if (ObjectUtils.isEmpty(employee)) {
            throw new RuntimeException("该用户未绑定员工");
        }
        return employee.getId();
    }

    @Override
    public Result<Employee> getEmployeeInfo() {
        long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUserId, userId);
        return getOne(wrapper) == null ? Result.fail(40000, "该用户未绑定员工") : Result.success(getOne(wrapper));
    }
}




