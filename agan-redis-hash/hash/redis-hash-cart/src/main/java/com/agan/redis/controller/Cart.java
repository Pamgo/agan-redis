package com.agan.redis.controller;

import lombok.Data;

import java.io.Serializable;

@Data
public class Cart {

    private Long userId;
    private Long productId;
    private int amount;

}
 