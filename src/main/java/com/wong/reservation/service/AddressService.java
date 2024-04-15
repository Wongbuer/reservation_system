package com.wong.reservation.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wong.reservation.domain.dto.AddressDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Address;

import java.util.List;

/**
 * @author Wongbuer
 * @description 针对表【address】的数据库操作Service
 * @createDate 2024-04-12 17:52:50
 */
public interface AddressService extends IService<Address> {
    /**
     * 根据用户ID获取所有地址信息
     *
     * @return Result<List<Address>> 地址信息列表
     */
    Result<List<Address>> getAllAddressWithLoginUserId();

    /**
     * 添加地址
     *
     * @param addressDTO 地址信息
     * @return Result<?> 添加结果
     */
    Result<?> addAddress(AddressDTO addressDTO);

    /**
     * 修改地址
     *
     * @param address 地址信息
     * @return Result<?> 修改结果
     */
    Result<?> updateAddress(Address address);

    /**
     * 删除地址
     *
     * @param id 地址ID
     * @return Result<?> 删除结果
     */
    Result<?> deleteAddress(Long id);
}
