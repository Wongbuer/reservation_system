package com.wong.reservation.utils.lock;

/**
 * @author Wongbuer
 * @createDate 2024/4/19
 */
public interface OrderLock {
    boolean acquireLock(String key, long waitTime);
    void releaseLock(String key);
    boolean isLocked(String key);
}
