package com.agan.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class IdGenerator {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String ID_KEY = "id:generator:cart";

    /**
     * 生成全局唯一id
     */
    public Long incrementId() {
        long n=this.stringRedisTemplate.opsForValue().increment(ID_KEY);
        return n;
    }

}
