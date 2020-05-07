package com.agan.weibo.service;


import com.agan.weibo.common.Constants;
import com.agan.weibo.controller.ContentVO;
import com.agan.weibo.controller.UserVO;
import com.agan.weibo.entity.Content;
import com.agan.weibo.entity.User;
import com.agan.weibo.mapper.UserMapper;
import com.agan.weibo.utils.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@Slf4j
@Service
public class RelationService {



    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;



    /**
     * 阿甘关注了雷军
     */
    public void follow(Integer userId,Integer followeeId){
        SetOperations<String, Integer> opsForSet = redisTemplate.opsForSet();
        //阿甘的关注集合
        String followeekey = Constants.CACHE_KEY_FOLLOWEE + userId;
        //把雷军的followeeId，加入阿甘的关注集合中
        opsForSet.add(followeekey,followeeId);

        //雷军的粉丝集合
        String followerkey=Constants.CACHE_KEY_FOLLOWER+followeeId;
        //把阿甘的userid加入雷军的粉丝follower集合set
        opsForSet.add(followerkey,userId);

    }

    /**
     * 查看我的粉丝
     */
    public List<UserVO> myFollower(Integer userId){
        SetOperations<String, Integer> opsForSet = redisTemplate.opsForSet();
        //粉丝集合
        String followerkey=Constants.CACHE_KEY_FOLLOWER+userId;
        Set<Integer> sets= opsForSet.members(followerkey);

        return this.getUserInfo(sets);
    }

    /**
     * 查看我的关注
     */
    public List<UserVO> myFollowee(Integer userId){
        SetOperations<String, Integer> opsForSet = redisTemplate.opsForSet();
        //关注集合
        String followeekey = Constants.CACHE_KEY_FOLLOWEE + userId;
        Set<Integer> sets= opsForSet.members(followeekey);
        return this.getUserInfo(sets);
    }

    /**
     * 求2个用户的关注交集
     */
    public List<UserVO> intersect(Integer userId1,Integer userId2){
        SetOperations<String, Integer> opsForSet = redisTemplate.opsForSet();

        String followeekey1 = Constants.CACHE_KEY_FOLLOWEE + userId1;
        String followeekey2 = Constants.CACHE_KEY_FOLLOWEE + userId2;
        //求2个集合的交集
        Set<Integer> sets= opsForSet.intersect(followeekey1,followeekey2);
        return this.getUserInfo(sets);
    }


    /**
     * 获取用户信息
     */
    private List<UserVO> getUserInfo(Set<Integer> set){
        List<UserVO> list=new ArrayList<>();

        List<String> hashKeys=new ArrayList<>();
        hashKeys.add("id");
        hashKeys.add("username");

        HashOperations<String, String ,Object> opsForHash=redisTemplate.opsForHash();
        for (Integer id:set) {
            String hkey = Constants.CACHE_KEY_USER + id;
            List<Object> clist = opsForHash.multiGet(hkey, hashKeys);
            //redis没有去db找
            if (clist.get(0)==null && clist.get(1)==null){
                //到数据库查
                User user=this.getUserDB(id);

                UserVO vo=new UserVO();
                vo.setId(user.getId());
                vo.setUsername(user.getUsername());
                list.add(vo);
            }else{
                UserVO vo=new UserVO();
                vo.setId(clist.get(0)==null?0:Integer.valueOf(clist.get(0).toString()));
                vo.setUsername(clist.get(1)==null?"":clist.get(1).toString());
                list.add(vo);
            }
        }
        return list;
    }

    public User getUserDB(Integer userid) {
        User obj = this.userMapper.selectByPrimaryKey(userid);
        //将Object对象里面的属性和值转化成Map对象
        Map<String, Object> map = ObjectUtil.objectToMap(obj);
        //设置缓存key
        String key = Constants.CACHE_KEY_USER + obj.getId();

        //微博用户的存储采用reids的hash
        HashOperations<String, String, Object> opsForHash = redisTemplate.opsForHash();
        opsForHash.putAll(key, map);

        //设置过期30天
        this.redisTemplate.expire(key, 30, TimeUnit.DAYS);
        return obj;
    }
}
