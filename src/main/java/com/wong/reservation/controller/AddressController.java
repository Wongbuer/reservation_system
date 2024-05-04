package com.wong.reservation.controller;

import com.wong.reservation.domain.dto.AddressDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Address;
import com.wong.reservation.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Wongbuer
 * @createDate 2024/4/12
 */
@RestController
@RequestMapping(value = "/address", method = {RequestMethod.GET, RequestMethod.POST})
public class AddressController {
    @Resource
    private AddressService addressService;

    /**
     * 根据登录用户ID获取其所有地址信息
     *
     * @return 地址信息
     */
    @Operation(summary = "根据登录用户ID获取其所有地址信息")
    @GetMapping("/list")
    public Result<List<Address>> getAllAddressWithLoginUserId() {
        return addressService.getAllAddressWithLoginUserId();
    }

    /**
     * 添加地址
     *
     * @param addressDTO 地址信息包装类
     * @return 添加结果
     */
    @Operation(summary = "添加地址")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<?> addAddress(@RequestBody AddressDTO addressDTO) {
        return addressService.addAddress(addressDTO);
    }

    /**
     * 修改地址
     *
     * @param address 地址信息
     * @return 修改结果
     */
    @Operation(summary = "修改地址")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result<?> updateAddress(@RequestBody Address address) {
        return addressService.updateAddress(address);
    }

    /**
     * 删除地址
     *
     * @param id 地址ID
     * @return 删除结果
     */
    @Operation(summary = "删除地址")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result<?> deleteAddress(Long id) {
        return addressService.deleteAddress(id);
    }

    /**
     * 设置默认地址
     *
     * @param id 地址ID
     * @return Result<?> 设置结果
     */
    @Operation(summary = "设置默认地址")
    @RequestMapping(value = "/setDefault", method = RequestMethod.GET)
    public Result<?> setDefaultAddress(Long id) {
        return addressService.setDefaultAddress(id);
    }
}
