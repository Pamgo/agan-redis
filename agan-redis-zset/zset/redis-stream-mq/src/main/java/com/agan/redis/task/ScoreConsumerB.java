package com.agan.redis.task;

import com.agan.redis.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@Service
@Slf4j
public class ScoreConsumerB {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 消费任务
     */
    @PostConstruct
    public void consume() {
        log.info("启动消费 ..........");
        new Thread(() -> this.consumeData()).start();
    }


    public void consumeData() {

        /*
         * 由于redisTemplate不支持检查group是否存在，同时也不支持创建XGROUP CREATE mq-order group-score $ MKSTREAM
         * 故；
         * 我们在启动程序之前，要在redis执行以下命令，先创建消息组，不然程序找不到消息组
         * XGROUP CREATE mq-order group-score $ MKSTREAM
         * 切记 不然会报错  NOGROUP No such key 'mq-order' or consumer group 'group-score' in XREADGROUP with GROUP option
         */
        StreamOffset streamOffset = StreamOffset.create(Constants.MQ_ORDER, ReadOffset.lastConsumed());
        //创建一个消费者
        Consumer consumer = Consumer.from(Constants.GROUP_SCORE, Constants.CONSUMER_SCORE);
        //block 阻塞 60s读
        StreamReadOptions streamReadOptions = StreamReadOptions.empty().block(Duration.ofMinutes(60));
        while (true) {
            //XREADGROUP GROUP group-score consumer-score  block 60000 STREAMS mq-order >
            List<MapRecord<String, String, String>> list = this.redisTemplate.opsForStream().read(consumer, streamReadOptions, streamOffset);
            for (MapRecord<String, String, String> obj : list) {
                log.info("B积分服务消费了{}", list);
                //TODO 添加积分逻辑

                //通知消息处理结束，用消息ID标识
                //xack ordermq ordergroup 1588560150589-0
                this.redisTemplate.opsForStream().acknowledge(Constants.MQ_ORDER, Constants.GROUP_SCORE, obj.getId());
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
