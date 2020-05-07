package com.agan.redis.controller;

import com.agan.redis.Reentrant.ReentrantLockDemo;
import com.agan.redis.Reentrant.SynchronizedDemo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@RestController
@Slf4j
public class ReentrantController {


    ReentrantLockDemo reentrantLockDemo=new ReentrantLockDemo();



    @GetMapping(value = "/lock2")
    public void lock2(String key) {
        log.info("-------请求{}--------",key);
        this.reentrantLockDemo.doSomething(1);
        log.info("--------请求{}结束--------",key);
    }

}
