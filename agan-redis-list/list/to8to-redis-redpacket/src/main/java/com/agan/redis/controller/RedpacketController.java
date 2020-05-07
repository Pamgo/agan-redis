package com.agan.redis.controller;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.util.resources.ga.LocaleNames_ga;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@RestController
@Slf4j
@RequestMapping(value = "/red")
public class RedpacketController {

    @Autowired
    private RedisTemplate redisTemplate;
    static final String RED_PACKET_CONSUME_KEY="redpacket:consume:";
    static final String RED_PACKET_KEY="redpacket:";
    static final String ID_KEY = "id:generator:redpacket";

    /**
     * 抢红包接口
     */
    @GetMapping(value = "/rob")
    public int rob(long redid,long userid) {
        //第一步：验证该用户是否抢过
        Object packet=this.redisTemplate.opsForHash().get(RED_PACKET_CONSUME_KEY+redid,String.valueOf(userid));
        if(packet==null){
            //第二步：从list队列，弹出一个红包
            Object obj=this.redisTemplate.opsForList().leftPop(RED_PACKET_KEY+redid);
            if(obj!=null){
                //第三步：抢到红包存起来
                this.redisTemplate.opsForHash().put(RED_PACKET_CONSUME_KEY+redid,String.valueOf(userid),obj);
                log.info("用户={}抢到{}",userid,obj);
                //TODO 异步把数据落地到数据库上
                return (Integer) obj;
            }
            //-1 代表抢完
            return -1;
        }
        //-2 该用户代表已抢
        return -2;
    }

    /**
     * 包红包的接口
     */
    @GetMapping(value = "/set")
    public long setRedpacket(int total, int count) {
        //拆解红包
        Integer[] packet= this.splitRedPacket(total,count);
        //为红包生成全局唯一id
        long n=this.incrementId();
        //采用list存储红包
        String key=RED_PACKET_KEY+n;
        this.redisTemplate.opsForList().leftPushAll(key,packet);
        //设置3天过期
        this.redisTemplate.expire(key,3, TimeUnit.DAYS);
        log.info("拆解红包{}={}",key,packet);
        return n;
    }

    /**
     * 生成全局唯一id
     * @return
     */
    public Long incrementId() {
        long n=this.redisTemplate.opsForValue().increment(ID_KEY);
        return n;
    }

    /**
     * 拆解红包
     * 1.红包金额要被全部拆解完
     * 2.红包金额不能差太离谱
     */
    public  Integer[] splitRedPacket(int total, int count) {
        int use = 0;
        Integer[] array = new Integer[count];
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            if (i == count - 1)
                array[i] = total - use;
            else {
                // 2 红包随机金额浮动系数
                int avg = (total - use) * 2 / (count - i);
                array[i] = 1 + random.nextInt(avg - 1);
            }
            use = use + array[i];
        }
        return array;
    }

}












