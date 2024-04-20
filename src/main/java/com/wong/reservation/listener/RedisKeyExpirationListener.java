package com.wong.reservation.listener;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wong.reservation.constant.OrderStatusConstant;
import com.wong.reservation.domain.entity.Order;
import com.wong.reservation.service.OrderService;
import com.wong.reservation.utils.lock.OrderLock;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import static com.wong.reservation.constant.SystemConstant.*;

/**
 * @author Wongbuer
 * @createDate 2024/4/18
 */
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    @Resource
    private OrderService orderService;
    @Resource
    private OrderLock orderLock;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    protected void doHandleMessage(Message message) {
        String redisKey = message.toString();
        log.info("redis key: {} expired", redisKey);
        // 判断是否为过期未接受/过期未付款
        String orderId = null;
        String type = null;
        if (redisKey.startsWith(ORDER_CREATED_PREFIX)) {
            type = ORDER_LOCK_ACCEPT_PREFIX;
            // 获取value
            orderId = redisKey.substring(ORDER_CREATED_PREFIX.length());
        } else if (redisKey.startsWith(ORDER_ACCEPTED_PREFIX)) {
            type = ORDER_LOCK_PAY_PREFIX;
            // 获取value
            orderId = redisKey.substring(ORDER_ACCEPTED_PREFIX.length());
        } else {
            return;
        }
        // 获取订单
        String lockKey = type + orderId;
        boolean isLocked = orderLock.acquireLock(lockKey, 3000);
        if (isLocked) {
            try {
                Order order = orderService.getById(orderId);
                if (ObjectUtils.isEmpty(order)) {
                    throw new RuntimeException("订单不存在");
                }

                LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(Order::getId, orderId);

                // 判断是否为CREATED
                if (order.getStatus() == OrderStatusConstant.CREATED) {
                    wrapper
                            // 再次确认订单状态正确
                            .eq(Order::getStatus, OrderStatusConstant.CREATED)
                            // 修改订单状态
                            .set(Order::getStatus, OrderStatusConstant.TIMEOUT_CREATED);
                    updateOrderStatus(wrapper);
                } else if (order.getStatus() == OrderStatusConstant.ACCEPTED) {
                    wrapper
                            // 再次确认订单状态正确
                            .eq(Order::getStatus, OrderStatusConstant.ACCEPTED)
                            // 修改订单状态
                            .set(Order::getStatus, OrderStatusConstant.TIMEOUT_PAYMENT);
                    updateOrderStatus(wrapper);
                } else {
                    log.info("订单状态已变化, 已经为: {}", OrderStatusConstant.getStatusName(order.getStatus()));
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            } finally {
                orderLock.releaseLock(lockKey);
            }
        } else {
            log.info("锁竞争失败——订单状态应该已经发生变化");
        }
    }

    private void updateOrderStatus(LambdaUpdateWrapper<Order> wrapper) {
        try {
            boolean isUpdated = orderService.update(wrapper);
            if (!isUpdated) {
                log.info("订单状态已变化");
            } else {
                log.info("订单状态修改成功");
            }
        } catch (Exception e) {
            log.error("修改订单状态失败", e);
        }
    }
}
