package com.wong.reservation.utils.lock.impl.local;

import com.wong.reservation.utils.lock.OrderLock;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Wongbuer
 * @createDate 2024/4/19
 */
@Getter
public abstract class ReentrantOrderLock implements OrderLock {
    private final Map<String, Lock> lockMap;

    protected ReentrantOrderLock(Map<String, Lock> lockMap) {
        this.lockMap = lockMap;
    }

    @Override
    public boolean acquireLock(String key, long waitTime) {
        Pattern pattern = Pattern.compile("(?<=:)(\\w+:\\d+)");
        Matcher matcher = pattern.matcher(key);
        String lockKey = null;
        if (matcher.find()) {
            lockKey = matcher.group();
        } else {
            throw new IllegalArgumentException("可重入锁key异常");
        }
        Lock lock = lockMap.computeIfAbsent(lockKey, k -> new ReentrantLock());
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
        Pattern pattern = Pattern.compile("(?<=:)(\\w+:\\d+)");
        Matcher matcher = pattern.matcher(key);
        String lockKey = null;
        if (matcher.find()) {
            lockKey = matcher.group();
        } else {
            throw new IllegalArgumentException("订单id异常");
        }
        Lock lock = lockMap.get(lockKey);
        if (lock != null) {
            lock.unlock();
        }
    }

    @Override
    public boolean isLocked(String key) {
        Pattern pattern = Pattern.compile("(?<=:)(\\w+:\\d+)");
        Matcher matcher = pattern.matcher(key);
        String lockKey = matcher.group();
        if (lockMap.get(lockKey) instanceof ReentrantLock) {
            return ((ReentrantLock) lockMap.get(lockKey)).isLocked();
        } else {
            return false;
        }
    }

    public abstract void removeLocks();
}
