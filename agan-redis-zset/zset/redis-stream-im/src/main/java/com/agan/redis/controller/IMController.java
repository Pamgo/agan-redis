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
public class IMController {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     *初始化房间号
     */
    @GetMapping(value = "/init")
    public long init() {
        long roomid= this.redisTemplate.opsForValue().increment("roomid");
        return roomid;
    }

    /**
     * 发送消息
     * xadd key ID field string [field string ...]
     */
    @GetMapping(value = "/add")
    public RecordId add(String roomid ,String username ,String centent) {
        String roomkey=Constants.ROOM_KEY+roomid;
        Map<String,String> map=new HashMap<>();
        map.put(username,centent);
        //redis xadd
        RecordId recordId =this.redisTemplate.opsForStream().add(roomkey,map);
        return recordId;
    }

    /**
     * 监听消息
     * xread [COUNT count] [BLOCK milliseconds] STREAMS key [key ...] ID [ID ...]
     */
    @GetMapping(value = "/read")
    public List<MapRecord<String,String,String>>  read(String roomid ) {
        //
        String roomkey=Constants.ROOM_KEY+roomid;
        //ReadOffset.latest()$代表是一个特殊起始ID读取消息，表示只接收实时最新的频道消息
        //ReadOffset.from("ID")ID代表来取该ID之后的消息。
        StreamOffset streamOffset=StreamOffset.create(roomkey, ReadOffset.latest());
        //BLOCK 阻塞监听，本次模拟用了10分钟阻塞。
        StreamReadOptions streamReadOptions=StreamReadOptions.empty().block(Duration.ofMinutes(10));
        return this.redisTemplate.opsForStream().read(streamReadOptions,streamOffset);
    }

}












