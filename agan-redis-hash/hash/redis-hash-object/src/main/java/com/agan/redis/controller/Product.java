package com.agan.redis.controller;

import lombok.Data;

import java.io.Serializable;

@Data
public class Product {
    
    private Long id;
    /**
     * 产品名称
     */
    private String name;
    /**
     * 产品价格
     */
    private Integer price;
    /**
     * 产品详情
     */
    private String detail;

}
 