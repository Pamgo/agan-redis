package com.agan.redis.controller;

import lombok.Data;

import java.util.List;

@Data
public class CartPage<T> {

    private List<T> cartList;
    private int count;

}
 