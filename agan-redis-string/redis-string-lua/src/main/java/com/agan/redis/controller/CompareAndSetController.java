package com.agan.redis.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@RestController
@Slf4j
public class CompareAndSetController {

    @Resource
    private DefaultRedisScript<Long> compareAndSetScript;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 修改用户名称
     */
    @GetMapping(value = "/updateuser")
    public void updateUser(Integer uid,String uname) {
        String key="user:"+uid;
        //优化点：第一次发送redis请求
        String old=this.stringRedisTemplate.opsForValue().get(key);
        if(StringUtils.isEmpty(old)){
            //优化点：第二次发送redis请求
            this.stringRedisTemplate.opsForValue().set(key,uname);
            return;
        }
        if(old.equals(uname)){
            log.info("{}不用修改", key);
        }else{
            log.info("{}从{}修改为{}", key,old,uname);
            //优化点：第二次发送redis请求
            this.stringRedisTemplate.opsForValue().set(key,uname);
        }
        /*
        以上代码，看似简单，但是在高并发的情况下，还是有一点性能瓶颈，在性能方面主要是发送了2次redis请求。
        那如何优化呢？
        我们可以采用lua技术，把2次redis请求合成一次。
         */
    }


    @GetMapping(value = "/updateuserlua")
    public void updateUserLua(Integer uid,String uname) {
        String key="user:"+uid;
        //设置redis的key
        List<String> keys = Arrays.asList(key);
        //执行lua脚本，execute方法有3个参数，第一个参数是lua脚本对象，第二个是key列表，第三个是lua的参数数组
        Long n = this.stringRedisTemplate.execute(this.compareAndSetScript, keys, uname);
        if (n == 0) {
            log.info("{}不用修改", key);
        } else {
            log.info("{}修改为{}", key,uname);
        }
    }




}
