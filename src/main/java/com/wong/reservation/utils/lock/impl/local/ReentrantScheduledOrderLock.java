package com.wong.reservation.utils.lock.impl.local;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wong.reservation.constant.OrderStatusConstant;
import com.wong.reservation.domain.entity.Order;
import com.wong.reservation.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wongbuer
 * @createDate 2024/4/19
 */
@Slf4j
public class ReentrantScheduledOrderLock extends ReentrantOrderLock {
    public final OrderMapper orderMapper;

    public ReentrantScheduledOrderLock(OrderMapper orderMapper) {
        super(new HashMap<>());
        this.orderMapper = orderMapper;
    }

    @Override
    @Scheduled(cron = "*/30 * * * *")
    public void removeLocks() {
        if (log.isInfoEnabled()) {
            log.trace("定时任务启动, 开始清除终止状态订单锁");
        }
        List<Integer> terminalStatus = OrderStatusConstant.getTerminalStatus();
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Order::getStatus, terminalStatus);
        // 获取终止状态的所有订单
        List<Order> orders = orderMapper.selectList(wrapper);
        for (Order order : orders) {
            // 根据员工id去除可重入锁
            getLockMap().remove("create:" + order.getEmployeeId());
            // 根据订单id去除可重入锁
            getLockMap().remove("accept:" + order.getId());
            getLockMap().remove("pay:" + order.getId());
        }
        // 生成终止状态订单ID列表
        ArrayList<Long> terminalStatusOrderIdList = orders.stream().map(Order::getId).collect(Collectors.toCollection(ArrayList::new));
        if (log.isInfoEnabled()) {
            log.trace("定时任务结束, 清除终止状态订单锁完成, 清除锁id: {}", terminalStatusOrderIdList);
        }
    }
}
