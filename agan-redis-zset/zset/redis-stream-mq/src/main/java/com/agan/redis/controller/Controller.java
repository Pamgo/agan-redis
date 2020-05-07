package com.agan.redis.controller;

import com.agan.redis.config.Constants;
import com.google.common.collect.Maps;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@RestController
@Slf4j
public class Controller {

    @Autowired
    private RedisTemplate redisTemplate;




    /**
     * 发送消息
     */
    @GetMapping(value = "/add")
    public RecordId add(String orderno) {
        Map<String,String> map=new HashMap<>();
        map.put("orderno",orderno);
        RecordId recordId =this.redisTemplate.opsForStream().add(Constants.MQ_ORDER,map);
        return recordId;
    }


}












