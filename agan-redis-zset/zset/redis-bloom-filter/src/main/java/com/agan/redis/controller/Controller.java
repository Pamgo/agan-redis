package com.agan.redis.controller;

import com.agan.redis.config.Constants;
import com.google.common.collect.Maps;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import io.rebloom.client.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.*;

@RestController
@Slf4j
public class Controller {

    @Autowired
    private Client rebloomClient ;

    /**
     * 今天头条的每次推荐 18条数据
     */
    final  int SIZE=18;

    /**
     * 拉取18条推荐数据
     */
    @GetMapping(value = "/rebloom")
    public List<Integer> rebloom(String userid) {
        String key=Constants.REBLOOM+userid;
        List<Integer> list=new ArrayList<>();
        while (true){
            //步骤1：到 大数据信息流服务 获取18条数据
            List<Integer>  ids=this.getArticleIds();

            //步骤2：到布隆过滤器 过滤已读的数据
            String [] array=new String[ids.size()];
            for(int i=0;i<ids.size();i++) {
                array[i]=String.valueOf(ids.get(i));
            }
            boolean [] bos=this.rebloomClient.existsMulti(key,array);

            for (int i=0;i<ids.size();i++){
                boolean bo=bos[i];
                //bo=true:布隆过滤器存在， false布隆过滤器不存在
                if(!bo){
                    list.add(ids.get(i));
                }else{
                    log.debug("过滤{}",ids.get(i));
                }
                //达到18条  就退出死循环
                if(list.size()==SIZE){
                    break;
                }
            }
            if(list.size()==SIZE){
                break;
            }
        }

        //步骤3：把本次的数据加入到布隆过滤器里面，代表已读
        String [] array=new String[list.size()];
        for(int i=0;i<list.size();i++) {
            array[i]=String.valueOf(list.get(i));
        }
        this.rebloomClient.addMulti(key,array);
        return list;
    }

    /**
     * 模拟大数据 信息流服务
     * 获取18条数据，
     */
    public List<Integer> getArticleIds() {
        Random rand = new Random();
        List<Integer> list=new ArrayList<>();
        for(int i=0;i<SIZE;i++) {
            int id=rand.nextInt(100000);
            list.add(id);
        }
        return list;
    }


    /**
     * 为某个用户 模拟1万条 已读数据
     */
    @GetMapping(value = "/init")
    public void init(Integer userid) {
        String [] array=new String[10000];
        for(int i=0;i<10000;i++) {
            array[i]=String.valueOf(i);
        }
        this.rebloomClient.addMulti(Constants.REBLOOM+userid,array);
    }

}












