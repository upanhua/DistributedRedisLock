package com.xuecheng.manage_cms.dao;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public class DistributedRedisLock {
    private static RedissonClient redisson = RedissonManager.getRedisson();
    private static final String LOCK_TITLE = "redisLock_";

    public static void acquire(String lockName){
        String key = LOCK_TITLE + lockName;
        RLock mylock = redisson.getLock(key);
        mylock.lock(2, TimeUnit.MINUTES); //lock提供带timeout参数，timeout结束强制解锁，防止死锁
        System.err.println("======lock======"+Thread.currentThread().getName());
    }

    public static void release(String lockName){
        String key = LOCK_TITLE + lockName;
        RLock mylock = redisson.getLock(key);
        mylock.unlock();
        System.err.println("======unlock======"+Thread.currentThread().getName());
    }

    public static void main(String[] args){
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String key = "test123";
                        DistributedRedisLock.acquire(key);
                        Thread.sleep(2000); //获得锁之后可以进行相应的处理
                        System.err.println("======获得锁后进行相应的操作======");
                        DistributedRedisLock.release(key);
                        System.err.println("=============================");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }
}
