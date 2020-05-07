package com.agan.redis.controller;

import com.agan.redis.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@Slf4j
@RestController
@RequestMapping(value = "/redis")
public class RedisController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @PostMapping(value = "/createOrder", produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public String createOrder(@RequestBody  OrderDTO obj) {

        //步骤1：先转换为唯一MD5
        String json=JsonUtil.object2Json(obj);
        String md5 = DigestUtils.md5DigestAsHex(json.getBytes()).toUpperCase();

        //步骤2：把md5设置为分布式锁的key
        /**
         * setIfAbsent 的作用就相当于 SET key value [NX] [XX] [EX <seconds>] [PX [millseconds]]
         * 设置 5分钟过期
         */
        Boolean bo=stringRedisTemplate.opsForValue().setIfAbsent(md5,"1",60*5, TimeUnit.SECONDS);
        if(bo){
            // 加锁成功
            log.debug("{}拿锁成功，开始处理业务",md5);
            try {
                //模拟10秒 业务处理
                Thread.sleep(1000*10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //步骤3：解锁
            stringRedisTemplate.delete(md5);
            log.debug("{}拿锁成功，结束处理业务",md5);
            return "ok";
        }else{
            log.debug("{}拿锁失败",md5);
            //拿不锁，直接退出
            return  "请不要重复点击！";
        }
    }

}
