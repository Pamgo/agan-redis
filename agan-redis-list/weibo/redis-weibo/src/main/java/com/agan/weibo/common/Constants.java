package com.agan.weibo.common;


public class Constants {

    public static final String CACHE_KEY_USER = "weibo:user:";

    /**
     * 阿甘关注了雷军
     * 阿甘的关注集合key=followee:阿甘userid
     * 雷军的粉丝集合key=follower:雷军userid
     */
    public static final String CACHE_KEY_FOLLOWEE = "followee:";
    /**
     * 粉丝集合
     */
    public static final String CACHE_KEY_FOLLOWER = "follower:";



    public static final String CACHE_CONTENT_KEY = "weibo:content:";
    /**
     * push
     */

    public  static final String CACHE_MY_POST_BOX_LIST_KEY="myPostBox:list:";
    public  static final String CACHE_MY_ATTENTION_BOX_LIST_KEY="myAttentionBox:list:";


    public  static final String CACHE_MY_POST_BOX_ZSET_KEY="myPostBox:zset:";
    public  static final String CACHE_MY_ATTENTION_BOX_ZSET_KEY="myAttentionBox:zset:";

    public  static final String CACHE_REFRESH_TIME_KEY="refresh:time:";


}