package com.agan.redis.config;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Constants {

    public  static final String CACHE_PV_LIST="pv:list";

    public  static final String CACHE_ARTICLE="article:";

    /**
     * Map<时间块，Map<文章Id,访问量>>
     * =Map<2020-01-12 15:30:00到 15:59:00，Map<文章Id,访问量>>
     * =Map<438560，Map<文章Id,访问量>>
     */
    public  static final  Map<Long, Map<Integer,Integer>> PV_MAP=new ConcurrentHashMap();

}