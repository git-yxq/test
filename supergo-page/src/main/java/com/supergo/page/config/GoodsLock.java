package com.supergo.page.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class GoodsLock {
    private ConcurrentHashMap<Long, ReentrantLock> lockMap = new ConcurrentHashMap<>();
    public ReentrantLock getLock(Long goodsId) {
        return lockMap.getOrDefault(goodsId, new ReentrantLock());
    }
    public void removeLock(Long goodsId) {
        lockMap.remove(goodsId);
    }
}
