package com.agan.weibo.common;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    /**
     * 总数
     */
    private long total;
     /**
     * 当页数据行集
     */
    private List<T> rows;

}
