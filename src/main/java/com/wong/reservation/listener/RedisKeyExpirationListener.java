package com.wong.reservation.listener;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wong.reservation.constant.OrderStatusConstant;
import com.wong.reservation.domain.entity.Order;
import com.wong.reservation.service.OrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import static com.wong.reservation.constant.SystemConstant.ORDER_ACCEPTED_PREFIX;
import static com.wong.reservation.constant.SystemConstant.ORDER_CREATED_PREFIX;

/**
 * @author Wongbuer
 * @createDate 2024/4/18
 */
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    @Resource
    private OrderService orderService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    protected void doHandleMessage(Message message) {
        String redisKey = message.toString();
        log.info("redis key: {} expired", redisKey);
        // 判断是否为过期未接受/过期未付款
        String orderId = null;
        if (redisKey.startsWith(ORDER_CREATED_PREFIX)) {
            // 获取value
            orderId = redisKey.substring(ORDER_CREATED_PREFIX.length());
        } else if (redisKey.startsWith(ORDER_ACCEPTED_PREFIX)) {
            // 获取value
            orderId = redisKey.substring(ORDER_ACCEPTED_PREFIX.length());
        } else {
            return;
        }
        // 获取订单
        Order order = orderService.getById(orderId);
        if (ObjectUtils.isEmpty(order)) {
            throw new RuntimeException("订单不存在");
        }

        LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Order::getId, orderId);

        // 判断是否为CREATED
        if (order.getStatus() == OrderStatusConstant.CREATED) {
            // 修改订单状态
            wrapper.set(Order::getStatus, OrderStatusConstant.TIMEOUT_CREATED);
        } else if (order.getStatus() == OrderStatusConstant.ACCEPTED) {
            wrapper.set(Order::getStatus, OrderStatusConstant.TIMEOUT_PAYMENT);
        } else {
            log.info("订单状态已变化: {}", order);
        }
        try {
            orderService.update(wrapper);
        } catch (Exception e) {
            log.error("修改订单状态失败", e);
        }
    }
}
