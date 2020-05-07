package com.agan.redis.controller;

import com.agan.redis.service.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@RestController
@Slf4j
@RequestMapping(value = "/pruduct")
public class ProductController {

    @Autowired
    private IdGenerator idGenerator;

    @PostMapping(value = "/create")
    public void create(Product obj) {
        //步骤1：生成分布式id
        long id=this.idGenerator.incrementId();
        //全局id，代替数据库的自增id
        obj.setId(id);

        //步骤2：取模，计算表名
        //类似于海量的数据，例如淘宝一般是分为1024张表，这里为了演示方便，只分为8张表。
        int table=(int)id % 8;
        String tablename="product_"+table;

        log.info("插入表名{}，插入内容{}",tablename,obj);
    }
}
