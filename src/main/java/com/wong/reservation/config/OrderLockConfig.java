package com.wong.reservation.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.wong.reservation.domain.properties.LockProperties;
import com.wong.reservation.mapper.OrderMapper;
import com.wong.reservation.service.OrderService;
import com.wong.reservation.utils.lock.OrderLock;
import com.wong.reservation.utils.lock.impl.distributed.RedissonOrderLock;
import com.wong.reservation.utils.lock.impl.local.ReentrantLruOrderLock;
import com.wong.reservation.utils.lock.impl.local.ReentrantScheduledOrderLock;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Wongbuer
 * @createDate 2024/4/19
 */
@Configuration
public class OrderLockConfig {
    @Resource
    private LockProperties lockProperties;

    @Bean
    public OrderLock orderLock(OrderMapper orderMapper) {
        if ("distributed".equals(lockProperties.getLockType())) {
            return new RedissonOrderLock();
        } else {
            if ("lru".equals(lockProperties.getLockRemoveType())) {
                return new ReentrantLruOrderLock(lockProperties.getLockRemoveSize());
            } else {
                return new ReentrantScheduledOrderLock(orderMapper);
            }
        }
    }

    @Bean
    public Snowflake snowflake() {
        return IdUtil.getSnowflake();
    }
}
