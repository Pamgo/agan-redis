package com.agan.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

//指定要扫描的Mapper类的包的路径
@MapperScan("com.agan.redis.mapper")
@SpringBootApplication
//@EnableCaching
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
