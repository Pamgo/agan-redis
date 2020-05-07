package com.agan.redis.service;

public class MainT {
    public static void main(String[] args) {
        String str="566ab90f";

        System.out.println("2进制: "+Long.toBinaryString(0x3FFFFFFF));
        //566ab90f
        System.out.println("2进制:"+Long.toBinaryString(0x566ab90f));
        System.out.println("格式化后:"+Long.toBinaryString(0x3fffffff & 0x566ab90f));


        System.out.println("0x0000003D：2进制:"+Long.toBinaryString(0x0000003D));
        System.out.println("0x0000003D：10进制:"+Long.parseLong("0000003D",16));

    }
}
