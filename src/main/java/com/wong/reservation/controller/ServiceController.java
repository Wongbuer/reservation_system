package com.wong.reservation.controller;

import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Service;
import com.wong.reservation.service.ServiceService;
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
@RequestMapping(value = "/service", method = {RequestMethod.GET, RequestMethod.POST})
public class ServiceController {
    @Resource
    private ServiceService serviceService;

    /**
     * 获取服务类型
     *
     * @return Result<List < Service>> 服务类型列表
     */
    @Operation(summary = "获取服务类型")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<List<Service>> getServiceList() {
        return Result.success(serviceService.list());
    }

    /**
     * 添加服务类型
     *
     * @param service 服务类型
     * @return Result<?> 添加结果
     */
    @Operation(summary = "添加服务类型")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<?> addService(@RequestBody Service service) {
        return serviceService.addService(service);
    }

    /**
     * 修改服务类型
     *
     * @param service 服务类型
     * @return Result<?> 修改结果
     */
    @Operation(summary = "修改服务类型")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result<?> updateService(@RequestBody Service service) {
        return serviceService.updateService(service);
    }

    /**
     * 删除服务类型
     *
     * @param id 服务类型ID
     * @return Result<?> 删除结果
     */
    @Operation(summary = "删除服务类型")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Result<?> deleteService(Long id) {
        return serviceService.removeById(id) ? Result.success("删除成功") : Result.fail("删除失败");
    }
}
