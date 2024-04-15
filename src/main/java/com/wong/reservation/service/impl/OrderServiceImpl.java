package com.wong.reservation.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wong.reservation.domain.entity.Order;
import com.wong.reservation.mapper.OrderMapper;
import com.wong.reservation.service.OrderService;
import org.springframework.stereotype.Service;

/**
* @author Wongbuer
* @description 针对表【orders】的数据库操作Service实现
* @createDate 2024-04-15 18:05:22
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}




