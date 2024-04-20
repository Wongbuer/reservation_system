package com.wong.reservation.utils.lock.impl.local;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * @author Wongbuer
 * @createDate 2024/4/19
 */
@Slf4j
public class ReentrantLruOrderLock extends ReentrantOrderLock{
    public ReentrantLruOrderLock(int maxSize) {
        super(new LinkedHashMap<>(maxSize){
            // 配置LRU
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Lock> eldest) {
                if (log.isTraceEnabled() && size() > maxSize) {
                    log.trace("订单锁达到配置额, 开始运行LRU清除最近最久未使用订单, 订单ID: {}", eldest.getKey());
                }
                return size() > maxSize;
            }
        });
    }

    @Override
    public void removeLocks() {
    }
}
