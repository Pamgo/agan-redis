package com.agan.weibo.service;


import com.agan.weibo.common.Constants;
import com.agan.weibo.entity.User;
import com.agan.weibo.mapper.UserMapper;
import com.agan.weibo.utils.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserService {



    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 微博注册
     */
    public void createUser(User obj) {
        //步骤1：先入库
        this.userMapper.insertSelective(obj);

        //步骤2：入库成功后，写入redis
        obj = this.userMapper.selectByPrimaryKey(obj.getId());
        //将Object对象里面的属性和值转化成Map对象
        Map<String, Object> map = ObjectUtil.objectToMap(obj);
        //设置缓存key
        String key = Constants.CACHE_KEY_USER + obj.getId();

        //微博用户的存储采用reids的hash
        HashOperations<String, String, Object> opsForHash = redisTemplate.opsForHash();
        opsForHash.putAll(key, map);

        //步骤3：设置过期30天
        this.redisTemplate.expire(key, 30, TimeUnit.DAYS);
    }

}
