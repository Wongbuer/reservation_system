package com.wong.reservation.controller;

import com.wong.reservation.domain.dto.EmployeeServiceDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Employee;
import com.wong.reservation.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**
     * 发布服务
     *
     * @param employeeServiceDTO 服务信息
     * @return 发布结果
     */
    @Operation(summary = "发布服务")
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public Result<?> publishService(@RequestBody EmployeeServiceDTO employeeServiceDTO) {
        return employeeService.publishService(employeeServiceDTO);
    }

    /**
     * 获取当前用户对应员工的服务列表
     *
     * @return Result<List<com.wong.reservation.domain.entity.EmployeeService>> 服务列表
     */
    @Operation(summary = "获取当前用户对应员工的服务列表")
    @RequestMapping(value = "/service/list", method = RequestMethod.GET)
    public Result<List<com.wong.reservation.domain.entity.EmployeeService>> getServiceList() {
        return employeeService.getServiceList();
    }

    /**
     * 删除服务
     *
     * @param id 员工服务ID(EmployeeServiceID)
     * @return 删除结果
     */
    @Operation(summary = "删除服务")
    @RequestMapping(value = "/service/delete", method = RequestMethod.GET)
    public  Result<?> deleteService(Long id) {
        return employeeService.deleteService(id);
    }
}
