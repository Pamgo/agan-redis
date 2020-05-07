package com.agan.redis.Reentrant;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@Slf4j
public class ReentrantLockDemo {

    private Lock lock =  new ReentrantLock();

    public void doSomething(int n){
        try{
            //进入递归第一件事：加锁
            lock.lock();
            log.info("--------递归{}次--------",n);
            if(n<=2){
                try {
                    Thread.sleep(1000*2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.doSomething(++n);
            }else{
                return;
            }
        }finally {
            lock.unlock();
        }
    }

}