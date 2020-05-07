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
public class SynchronizedController {

    SynchronizedDemo synchronizedDemo=new SynchronizedDemo();


    @GetMapping(value = "/lock1")
    public void lock1(String key) {
        log.info("-------用户{}开始下单--------",key);
        this.synchronizedDemo.operation();

    }

}
