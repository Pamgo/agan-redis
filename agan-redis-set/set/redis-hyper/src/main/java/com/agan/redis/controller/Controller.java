package com.agan.redis.controller;

import com.agan.redis.config.Constants;
import com.google.common.collect.Maps;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
public class Controller {

    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping(value = "/uv")
    public long uv() {
        //redis命令 pfcount
        long size=this.redisTemplate.opsForHyperLogLog().size(Constants.PV_KEY);
        return size;
    }


}












