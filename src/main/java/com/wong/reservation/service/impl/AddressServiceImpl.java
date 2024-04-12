package com.wong.reservation.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wong.reservation.domain.entity.Address;
import com.wong.reservation.mapper.AddressMapper;
import com.wong.reservation.service.AddressService;
import org.springframework.stereotype.Service;

/**
 * @author Wongbuer
 * @description 针对表【address】的数据库操作Service实现
 * @createDate 2024-04-12 17:52:50
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address>
        implements AddressService {

}




