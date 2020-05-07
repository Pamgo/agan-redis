package com.agan.redis.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@RestController
@Slf4j
public class RedisController {

    @Autowired
    RedissonClient redissonClient;

    @GetMapping(value = "/lock")
    public void get(String key) throws InterruptedException {
        this.getLock(key, 1);
    }

    private void getLock(String key, int n) throws InterruptedException {
        //模拟递归，3次递归后退出
        if (n > 3) {
            return;
        }
        //步骤1：获取一个分布式可重入锁RLock
        //分布式可重入锁RLock :实现了java.util.concurrent.locks.Lock接口，同时还支持自动过期解锁。
        RLock lock = redissonClient.getLock(key);
        //步骤2：尝试拿锁
        // 1. 默认的拿锁
        //lock.tryLock();
        // 2. 支持过期解锁功能,10秒钟以后过期自动解锁, 无需调用unlock方法手动解锁
        //lock.tryLock(10, TimeUnit.SECONDS);
        // 3. 尝试加锁，最多等待3秒，上锁以后10秒后过期自动解锁
        // lock.tryLock(3, 10, TimeUnit.SECONDS);
        boolean bs = lock.tryLock(3, 10, TimeUnit.SECONDS);
        if (bs) {
            try {
                // 业务代码
                log.info("线程{}业务逻辑处理: {},递归{}" ,Thread.currentThread().getName(), key,n);
                //模拟处理业务
                Thread.sleep(1000 * 5);
                //模拟进入递归
                this.getLock(key, ++n);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
            } finally {
                //步骤3：解锁
                lock.unlock();
                log.info("线程{}解锁退出",Thread.currentThread().getName());
            }
        } else {
            log.info("线程{}未取得锁",Thread.currentThread().getName());
        }
    }
}
