package com.wong.reservation.utils.lock.impl.local;

import com.wong.reservation.utils.lock.OrderLock;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Wongbuer
 * @createDate 2024/4/19
 */
@Getter
public abstract class ReentrantOrderLock implements OrderLock {
    private final Map<Long, Lock> lockMap;

    protected ReentrantOrderLock(Map<Long, Lock> lockMap) {
        this.lockMap = lockMap;
    }

    @Override
    public boolean acquireLock(String key, long waitTime) {
        // 正则表达式提取key中的Long型orderId
        Long orderId = Long.parseLong(key.replaceAll("[^0-9]", ""));
        Lock lock = lockMap.computeIfAbsent(orderId, k -> new ReentrantLock());
        try {
            return lock.tryLock(waitTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public void releaseLock(String key) {
        Long orderId = Long.parseLong(key.replaceAll("[^0-9]", ""));
        Lock lock = lockMap.get(orderId);
        if (lock != null) {
            lock.unlock();
        }
    }

    @Override
    public boolean isLocked(String key) {
        Long orderId = Long.parseLong(key.replaceAll("[^0-9]", ""));
        if (lockMap.get(orderId) instanceof ReentrantLock) {
            return ((ReentrantLock) lockMap.get(orderId)).isLocked();
        } else {
           return false;
        }
    }

    public abstract void removeLocks();
}
