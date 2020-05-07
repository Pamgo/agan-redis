package com.agan.redis.controller;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
public class User implements  Serializable{

    private int id;
    private String username;
    private String password;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

}
