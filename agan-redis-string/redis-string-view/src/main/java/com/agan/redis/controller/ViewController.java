package com.agan.redis.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class ViewController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = "/view")
    public void view(Integer id) {
        //redis key
        String key="article:"+id;
        //调用redis的increment计数器命令
        long n=this.stringRedisTemplate.opsForValue().increment(key);
        log.info("key={},阅读量为{}",key, n);
    }
}
