package com.wong.reservation.utils.lock.impl.local;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wong.reservation.constant.OrderStatusConstant;
import com.wong.reservation.domain.entity.Order;
import com.wong.reservation.mapper.OrderMapper;
import com.wong.reservation.service.OrderService;
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
        ArrayList<Long> terminalStatusOrderIdList = orderMapper.selectList(wrapper).stream().map(Order::getId).collect(Collectors.toCollection(ArrayList::new));
        terminalStatusOrderIdList.forEach(getLockMap()::remove);
        if (log.isInfoEnabled()) {
            log.trace("定时任务结束, 清除终止状态订单锁完成, 清除锁id: {}", terminalStatusOrderIdList);
        }
    }
}
