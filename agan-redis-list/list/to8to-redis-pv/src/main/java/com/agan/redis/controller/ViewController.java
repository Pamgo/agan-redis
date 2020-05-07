package com.agan.redis.controller;

import com.agan.redis.config.Constants;
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
    public String view(Integer id) {
        String key= Constants.CACHE_ARTICLE+id;
        //调用redis的get命令
        String n=this.stringRedisTemplate.opsForValue().get(key);
        log.info("key={},阅读量为{}",key, n);
        return n;
    }
}
