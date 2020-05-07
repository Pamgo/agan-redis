package com.agan.redis;

import com.agan.weibo.ServerApplication;
import com.agan.weibo.common.Constants;
import com.agan.weibo.entity.Content;
import com.agan.weibo.utils.ObjectUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServerApplication.class)
public class SpringBootMybatisApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
        for (int i=0;i<1000;i++){
            Content obj=new Content();
            obj.setId(i);
            obj.setContent("aaa"+i);
            Map<String, Object> map= ObjectUtil.objectToMap(obj);

            //缓存key
            String key= Constants.CACHE_CONTENT_KEY+obj.getId();
            //微博内容的redis数据结构采用hash
            HashOperations<String, String ,Object> opsForHash=redisTemplate.opsForHash();
            opsForHash.putAll(key,map);
        }
    }

    @Test
    public void getData() {
        HashOperations<String, String ,Object> opsForHash=redisTemplate.opsForHash();

        List<String> hashKeys=new ArrayList<>();
        hashKeys.add("content");
        hashKeys.add("userId");

        List<String> strlist=new ArrayList<>();
        for (int i=0;i<1000;i++){
            strlist.add(i+"");
            String key= Constants.CACHE_CONTENT_KEY+i;
            List<Object> list=opsForHash.multiGet(key,hashKeys);
            System.out.println(list);
        }

    }

    @Test
    public void getData2() {
        HashOperations<String, String ,Object> opsForHash=redisTemplate.opsForHash();

        List<String> hashKeys=new ArrayList<>();
        hashKeys.add("content");
        hashKeys.add("userId");

        for (int i=0;i<1;i++){
            String key= Constants.CACHE_CONTENT_KEY+300000;
            List<Object> list=opsForHash.multiGet(key,hashKeys);
            System.out.println(list);
        }

    }
}
