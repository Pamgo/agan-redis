package com.agan.weibo.service;


import com.agan.weibo.common.Constants;
import com.agan.weibo.common.PageResult;
import com.agan.weibo.entity.Content;
import com.agan.weibo.mapper.ContentMapper;
import com.agan.weibo.utils.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PushContentService extends ContentService{


    /**
     * 用户发微博
     */
    public void post(Content obj){
        this.addContent(obj);
        //1.push到个人主页
        this.pushHomeList(obj.getUserId(),obj.getId());
        //2.发一条微博，批量推送给所有粉丝
        this.pushFollower(obj.getUserId(),obj.getId());
    }

    /**
     * push到个人主页
     */
    public void pushHomeList(Integer userId,Integer postId){
        String key= Constants.CACHE_MY_POST_BOX_LIST_KEY+userId;
        this.redisTemplate.opsForList().leftPush(key,postId);
        //性能优化，截取前1000条
        if(this.redisTemplate.opsForList().size(key)>1000){
            this.redisTemplate.opsForList().trim(key,0,1000);
        }
    }

    /**
     * 获取个人主页列表
     */
    public PageResult<Content> homeList(Integer userId,int page, int size){
        PageResult<Content> pageResult=new PageResult();

        List<Integer> list=null;
        long start = (page - 1) * size;
        long end = start + size - 1;
        try {
            String key= Constants.CACHE_MY_POST_BOX_LIST_KEY+userId;
            //1.查询用户的总数
            int total=this.redisTemplate.opsForList().size(key).intValue();
            pageResult.setTotal(total);

            //2.采用redis list数据结构的lrange命令实现分页查询。
            list = this.redisTemplate.opsForList().range(key, start, end);

            //3.去拿明细
            List<Content> contents=this.getContents(list);
            pageResult.setRows(contents);
        }catch (Exception e){
            log.error("异常",e);
        }
        return pageResult;
    }

    /**
     * 发一条微博，批量推送给所有粉丝
     */
    private void pushFollower(int userId,int postId){
        SetOperations<String, Integer> opsForSet = redisTemplate.opsForSet();

        //读取粉丝集合
        String followerkey=Constants.CACHE_KEY_FOLLOWER+userId;
        //千万不能取set集合的所有数据，如果数据量大的话，会卡死
        // Set<Integer> sets= opsForSet.members(followerkey);
        Cursor<Integer> cursor = opsForSet.scan(followerkey, ScanOptions.NONE);
        try{
            while (cursor.hasNext()){
                //拿出粉丝的userid
                Integer object = cursor.next();
                String key= Constants.CACHE_MY_ATTENTION_BOX_LIST_KEY+object;
                this.redisTemplate.opsForList().leftPush(key,postId);
                //性能优化，截取前1000条
                if(this.redisTemplate.opsForList().size(key)>1000){
                    this.redisTemplate.opsForList().trim(key,0,1000);
                }
            }
        }catch (Exception ex){
            log.error("",ex);
        }finally {
            try {
                cursor.close();
            } catch (IOException e) {
                log.error("",e);
            }
        }
    }

    /**
     * 获取关注列表
     */
    public PageResult<Content> attentionList(Integer userId,int page, int size){
        PageResult<Content> pageResult=new PageResult();

        List<Integer> list=null;
        long start = (page - 1) * size;
        long end = start + size - 1;
        try {
            String key= Constants.CACHE_MY_ATTENTION_BOX_LIST_KEY+userId;
            //1.设置总数
            int total=this.redisTemplate.opsForList().size(key).intValue();
            pageResult.setTotal(total);

            //2.采用redis,list数据结构的lrange命令实现分页查询。
            list = this.redisTemplate.opsForList().range(key, start, end);

            //3.去拿明细数据
            List<Content> contents=this.getContents(list);
            pageResult.setRows(contents);
        }catch (Exception e){
            log.error("异常",e);
        }
        return pageResult;
    }

}
