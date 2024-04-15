package com.wong.reservation.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Order;

import java.util.List;

/**
 * @author Wongbuer
 * @description 针对表【order】的数据库操作Service
 * @createDate 2024-04-15 18:05:22
 */
public interface OrderService extends IService<Order> {
    /**
     * 获取订单(默认以创建时间降序排序)
     *
     * @param status 订单状态
     * @param sort   是否排序
     * @return Result<List < Order>>
     */
    Result<List<Order>> getOrderByUserId(String status, Boolean sort);

    /**
     * 添加订单
     *
     * @param order 订单
     * @return Result<?> 添加结果
     */
    Result<?> addOrder(Order order);

    /**
     * 更新订单
     *
     * @param order 订单
     * @return Result<?> 更新结果
     */
    Result<?> updateOrder(Order order);

    /**
     * 删除订单
     *
     * @param id 订单ID
     * @return Result<?> 删除结果
     */
    Result<?> deleteOrder(Long id);
}
