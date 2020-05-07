package com.agan.weibo.service;


import com.agan.weibo.common.Constants;
import com.agan.weibo.entity.Content;
import com.agan.weibo.mapper.ContentMapper;
import com.agan.weibo.utils.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@Slf4j
@Service
public abstract class ContentService {



    @Autowired
    protected ContentMapper contentMapper;

    @Autowired
    protected RedisTemplate redisTemplate;

    /**
     * 用户发微博
     */
    public Content addContent(Content obj){
        //步骤1：先入库
        this.contentMapper.insertSelective(obj);
        //步骤2：入库成功后 写redis
        obj=this.contentMapper.selectByPrimaryKey(obj.getId());
        //将Object对象里面的属性和值转化成Map对象
        Map<String, Object> map= ObjectUtil.objectToMap(obj);
        //设置缓存key
        String key= Constants.CACHE_CONTENT_KEY+obj.getId();
        //微博内容的redis数据结构 用hash
        HashOperations<String, String ,Object> opsForHash=redisTemplate.opsForHash();
        opsForHash.putAll(key,map);

        //步骤3：设置30天过期
        this.redisTemplate.expire(key,30, TimeUnit.DAYS);
        return obj;
    }


    protected List<Content> getContents(List<Integer> list){
        List<Content> contents=new ArrayList<>();

        //发布内容的key
        List<String> hashKeys=new ArrayList<>();
        hashKeys.add("id");
        hashKeys.add("content");
        hashKeys.add("userId");

        HashOperations<String, String ,Object> opsForHash=redisTemplate.opsForHash();
        for (Integer id:list){
            String hkey= Constants.CACHE_CONTENT_KEY+id;
            List<Object> clist=opsForHash.multiGet(hkey,hashKeys);
            //redis没有去db找
            if (clist.get(0)==null && clist.get(1)==null){
                Content obj=this.contentMapper.selectByPrimaryKey(id);
                contents.add(obj);
            }else{
                Content content=new Content();
                content.setId(clist.get(0)==null?0:Integer.valueOf(clist.get(0).toString()));
                content.setContent(clist.get(1)==null?"":clist.get(1).toString());
                content.setUserId(clist.get(2)==null?0:Integer.valueOf(clist.get(2).toString()));
                contents.add(content);
            }
        }
        return contents;
    }
}
