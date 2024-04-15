package com.wong.reservation.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wong.reservation.domain.dto.Result;
import com.wong.reservation.domain.entity.Order;
import com.wong.reservation.mapper.OrderMapper;
import com.wong.reservation.service.OrderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

/**
* @author Wongbuer
* @description 针对表【orders】的数据库操作Service实现
* @createDate 2024-04-15 18:05:22
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Override
    public Result<List<Order>> getOrderByUserId(String status, Boolean sort) {
        // 根据登录信息获取userId
        Long userId = (Long) StpUtil.getLoginId();
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        // 查询当前用户对应status下的所有订单, 如果status为null则为所有status的订单
        wrapper.eq(Order::getUserId, userId)
                .eq(status != null, Order::getStatus, status)
                .orderByDesc(sort, Order::getCreatedAt);
        List<Order> orderList = null;
        try {
            orderList = list(wrapper);
        } catch (Exception e) {
            // TODO: 日志处理
            e.printStackTrace();
            return Result.fail("获取订单失败");
        }
        return Result.success("获取订单成功", orderList);
    }

    @Override
    public Result<?> addOrder(Order order) {
        // 从登录信息获取userId
        Long userId = StpUtil.getLoginId(-1L);
        if (userId == -1L) {
            // 用户未登录
            return Result.token();
        }
        order.setUserId(userId);
        // TODO: 判断order是否含有必要参数(地址ID是否存在/服务ID是否存在/用户ID是否存在/员工ID是否存在/时间是否合法)
        // 判断是否含有相同的订单(相同指地址/服务/预约时间均相同)
        if (checkDuplicateOrder(order)) {
            return Result.fail("订单重复, 请勿重复下单");
        }
        // TODO: 判断员工时间是否冲突
        try {
            // 添加订单
            save(order);
        } catch (Exception e) {
            // TODO: 日志处理
            e.printStackTrace();
            return Result.fail(50000, "添加订单失败");
        }
        return Result.success("添加订单成功");
    }

    @Override
    public Result<?> updateOrder(Order order) {
        // 判断是否含有orderID
        if (order.getId() == null) {
            return Result.fail(40000, "订单ID不能为空");
        }
        // 从数据库获取订单信息, 补全order
        Order oldOrder = getById(order.getId());
        try {
            copyProperties(order, oldOrder);
            // 判断订单修改后是否重复
            if (checkDuplicateOrder(order)) {
                return Result.fail("订单重复, 请勿重复下单");
            }
            // 修改订单
            updateById(oldOrder);
        } catch (IllegalAccessException e) {
            // TODO: 日志处理
            e.printStackTrace();
            return Result.fail(50000, "订单信息不完整");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(50000, "修改订单失败");
        }
        return Result.success("修改订单成功");
    }

    @Override
    public Result<?> deleteOrder(Long id) {
        // 根据登录信息获取userId
        Long userId = StpUtil.getLoginId(-1L);
        if (userId == -1L) {
            // 用户未登录
            return Result.token();
        }
        // 删除订单
        boolean isDeleted = removeById(id);
        return isDeleted ? Result.success("删除订单成功") : Result.fail("删除订单失败");
    }

    private boolean checkDuplicateOrder(Order order) {
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper
                // 地址相同
                .eq(Order::getAddressId, order.getAddressId())
                // 服务内容
                .eq(Order::getServiceId, order.getServiceId())
                // 员工ID相同
                .eq(Order::getEmployeeId, order.getEmployeeId())
                // 用户ID相同
                .eq(Order::getUserId, order.getUserId())
                // 预约时间相同
                .eq(Order::getReservationTime, order.getReservationTime())
                // 订单未删除
                .eq(Order::getIsDeleted, 0);
        return count(orderWrapper) > 0;
    }

    private void copyProperties(Order newOrder, Order oldOrder) throws IllegalAccessException {
        for (Field field : Order.class.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(newOrder) == null) {
                field.set(newOrder, field.get(oldOrder));
            }
        }
    }
}



