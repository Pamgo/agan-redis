package com.agan.redis.controller;

import lombok.Data;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@Data
public class OrderDTO {
    private Integer id;
    //产品ID
    private Integer productId;
    //价格
    private Integer price;
    //用户账号ID
    private Integer userId;
    private Integer tradeId;//支付id
    private Integer tradeStatus;//支付状态
    private Integer deleted;//删除标志，默认0不删除，1删除
    private String createTime;
    private String updateTime;
}
