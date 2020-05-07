package com.agan.redis.controller;

import com.agan.redis.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@RestController
@Slf4j
public class ProductController {

    @Resource
    private DefaultRedisScript<Long> limitScript;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = "/productlist")
    public String productList(HttpServletRequest request) {
        //获取请求ip
        String ip = IpUtils.getIpAddr(request);
        //设置redis 的key
        List<String> keys = Arrays.asList("pruductAPI:" + ip);
        //执行lua脚本，execute方法有3个参数，第一个参数是lua脚本对象，第二个是key列表，第三个是lua的参数数组
        //30代表30秒 ，10代表超过10次，也就是说同个ip 30秒内不能超过10次请求
        Long n = this.stringRedisTemplate.execute(this.limitScript, keys, "30", "10");
        String result="";
        //非法请求
        if (n == 0) {
            result= "非法请求";
        } else {
            result= "返回商品列表";
        }
        log.info("ip={}请求结果：{}", ip,result);
        return result;
    }



}
