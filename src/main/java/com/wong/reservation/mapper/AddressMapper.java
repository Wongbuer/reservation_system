package com.wong.reservation.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wong.reservation.domain.entity.Address;

import java.util.List;

/**
 * @author Wongbuer
 * @description 针对表【address】的数据库操作Mapper
 * @createDate 2024-04-12 17:52:50
 * @Entity com.wong.reservation.domain.entity.Address
 */
public interface AddressMapper extends BaseMapper<Address> {
    List<Address> selectAddressListByUserId(String userId);
}




