package com.wong.reservation.controller;

import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Employee;
import com.wong.reservation.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wongbuer
 * @createDate 2024/4/17
 */
@RestController
@RequestMapping(value = "/employee", method = {RequestMethod.GET, RequestMethod.POST})
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;

    /**
     * 用户绑定员工信息
     *
     * @param employee 员工信息
     * @return 添加结果
     */
    @Operation(summary = "用户绑定员工信息")
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public Result<?> bindingEmployeeInfo(@RequestBody Employee employee) {
        return employeeService.bindingEmployeeInfo(employee);
    }
}
