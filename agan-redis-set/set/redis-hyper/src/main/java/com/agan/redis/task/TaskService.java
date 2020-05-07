package com.agan.redis.task;

import com.agan.redis.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TaskService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *模拟UV访问
     */
    @PostConstruct
    public void init(){
        log.info("模拟UV访问 ..........");
        new Thread(()->this.refreshData()).start();
    }

    /**
     * 刷新当天的统计数据
     */
    public void refreshDay(){
        Random rand = new Random();
        String ip=rand.nextInt(999)+"."+
                rand.nextInt(999)+"."+
                rand.nextInt(999)+"."+
                rand.nextInt(999);

        //redis 命令 pfadd
        long n=this.redisTemplate.opsForHyperLogLog().add(Constants.PV_KEY,ip);
        log.debug("ip={} , returen={}",ip,n);
    }

    /**
     * 按天模拟UV统计
     */
    public void refreshData(){
        while (true){
            this.refreshDay();
            //TODO 在分布式系统中，建议用xxljob来实现定时
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
