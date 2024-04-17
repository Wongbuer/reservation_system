package com.wong.reservation.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wong.reservation.domain.dto.AddressDTO;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Address;
import com.wong.reservation.mapper.AddressMapper;
import com.wong.reservation.service.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author Wongbuer
 * @description 针对表【address】的数据库操作Service实现
 * @createDate 2024-04-12 17:52:50
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Override
    public Result<List<Address>> getAllAddressWithLoginUserId() {
        // 根据当前登录用户获取用户ID
        Long loginId = StpUtil.getLoginIdAsLong();
        // 根据用户ID查询地址信息
        List<Address> addresses = baseMapper.selectAddressListByUserId(loginId);
        return Result.success("获取地址信息成功", addresses);
    }

    @Override
    public Result<?> addAddress(AddressDTO addressDTO) {
        try {
            Address address = BeanUtil.toBean(addressDTO, Address.class);
            boolean isSaved = save(address);
            Assert.isTrue(isSaved, "添加地址失败");
            return Result.success("添加地址成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("添加地址失败");
        }
    }

    @Override
    public Result<?> updateAddress(Address address) {
        Assert.notNull(address.getId(), "id不能为空");
        boolean isSaved = false;
        try {
            isSaved = updateById(address);
        } catch (Exception e) {
            return Result.fail("更新失败");
        }
        return isSaved ? Result.success("更新成功") : Result.fail("更新失败");
    }

    @Override
    public Result<?> deleteAddress(Long id) {
        Assert.notNull(id, "id不能为空");
        boolean isDeleted = false;
        try {
            isDeleted = removeById(id);
        } catch (Exception e) {
            return Result.fail("删除失败");
        }
        return isDeleted ? Result.success("删除成功") : Result.fail("删除失败");
    }
}




