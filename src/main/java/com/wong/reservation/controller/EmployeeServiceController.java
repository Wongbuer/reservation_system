package com.wong.reservation.controller;

import com.wong.reservation.domain.dto.EmployeeServiceDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.vo.EmployeeServiceVO;
import com.wong.reservation.service.EmployeeServiceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Wongbuer
 * @createDate 2024/4/25
 */
@RestController
@RequestMapping(value = "/employeeService", method = {RequestMethod.GET, RequestMethod.POST})
public class EmployeeServiceController {
    @Resource
    private EmployeeServiceService employeeServiceService;

    /**
     * 获取推荐服务
     *
     * @return Result<List<EmployeeService>>
     */
    @GetMapping("/recommend")
    @Operation(summary = "获取推荐服务")
    public Result<List<EmployeeServiceVO>> getRecommendServiceList() {
        return employeeServiceService.getRecommendServiceList();
    }

    /**
     * 根据服务类别Id获取该类别的员工服务
     *
     * @param serviceId 服务类别id
     * @return Result<List<EmployeeServiceVO>> 员工服务列表
     */
    @Operation(summary = "根据服务类别Id获取该类别的员工服务")
    @GetMapping("/list/{serviceId}")
    public Result<List<EmployeeServiceVO>> getServiceListByServiceId(@PathVariable("serviceId") Long serviceId, Integer current, Integer size) {
        return employeeServiceService.getServiceListByServiceId(serviceId, current, size);
    }

    /**
     * 发布服务
     *
     * @param employeeServiceDTO 员工服务DTO
     * @return Result<?> 发布结果
     */
    @PostMapping("/publish")
    @Operation(summary = "发布服务")
    public Result<?> publishService(@ModelAttribute EmployeeServiceDTO employeeServiceDTO) {
        return employeeServiceService.publishService(employeeServiceDTO);
    }

    /**
     * 获取当前用户对应员工的服务列表
     *
     * @return Result<List<com.wong.reservation.domain.entity.EmployeeService>> 服务列表
     */
    @Operation(summary = "获取当前用户对应员工的服务列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<List<com.wong.reservation.domain.entity.EmployeeService>> getServiceList() {
        return employeeServiceService.getServiceList();
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
        return employeeServiceService.deleteService(id);
    }
}
