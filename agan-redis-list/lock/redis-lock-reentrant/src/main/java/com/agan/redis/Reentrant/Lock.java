package com.agan.redis.Reentrant;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
public class Lock{
    //锁的状态：true=锁住，false=解锁
    private boolean isLocked = false;

    /**
     * 获取锁
     */
    public synchronized void lock() {
        while(isLocked){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isLocked = true;
    }

    /**
     * 解锁
     */
    public synchronized void unlock(){
        isLocked = false;
        notify();
    }
}
