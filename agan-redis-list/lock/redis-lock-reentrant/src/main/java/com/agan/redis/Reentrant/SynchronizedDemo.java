package com.agan.redis.Reentrant;

import lombok.extern.slf4j.Slf4j;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@Slf4j
public class SynchronizedDemo {
    //模拟库存100
    int count=100;
    public synchronized void operation(){
        log.info("第一层锁:减库存");
        //模拟减库存
        count--;
        add();
        log.info("下订单结束库存剩余:{}",count);
    }

    private synchronized void add(){
        log.info("第二层锁：插入订单");
        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

