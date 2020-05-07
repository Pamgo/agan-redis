package com.agan.redis.Reentrant;

import lombok.extern.slf4j.Slf4j;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@Slf4j
public class OrderDemo {
    Lock lock = new Lock();

    public void operation() {
        //加锁
        lock.lock();
        log.info("第一层锁:先减库存");
        //无法重入，进入锁等待，即死锁
        doAdd();
        lock.unlock();
    }

    public void doAdd() {
        //加锁
        lock.lock();
        log.info("第二层锁：插入订单");
        lock.unlock();
    }
    public static void main(String[] args){
        OrderDemo orderDemo=new OrderDemo();
        orderDemo.operation();
    }
}
