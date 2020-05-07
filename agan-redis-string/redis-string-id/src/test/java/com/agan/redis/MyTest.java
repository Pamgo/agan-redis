package com.agan.redis;

import java.util.Arrays;
import java.util.Random;

public class MyTest {

    public static void main(String[] args) {
        Integer[] redPacket = splitRedPacket(90, 5);
        Arrays.stream(redPacket).forEach(System.out::println);
    }

    public static Integer[] splitRedPacket(int total, int count) {

        int use = 0;
        Integer[] array = new Integer[count];
        Random random = new Random();
        for (int i =0 ; i < count; i++) {
            if (i == count - 1) {
                array[i] = total - use;
            } else {
                // 随机数浮动系数，避免浮动太大
                int avg = (total - use) * 2 / (count - i);
                System.out.println("avg:" + avg);
                array[i] = 1 + random.nextInt(avg - 1);
            }
            use = use + array[i];
        }

        return array;
    }
}
