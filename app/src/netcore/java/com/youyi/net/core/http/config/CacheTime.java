package com.youyi.net.core.http.config;

/**
 * 缓存时间
 * Created by Rain on 2016/2/3.
 */
public class CacheTime {
    /**
     * 无缓存
     */
    public static final int NONE_CACHE = 0;
    /**
     * 半分钟(ms)
     */
    public static final int HALF_MINUTE = 30 * 1000;
    /**
     * 1分钟
     */
    public static final int ONE_MINUTE = 2 * HALF_MINUTE;
    /**
     * 5分钟
     */
    public static final int FIVE_MINUTE = 5 * ONE_MINUTE;
    /**
     * 10分钟
     */
    public static final int TEN_MINUTE = 10 * ONE_MINUTE;
    /**
     * 15分钟
     */
    public static final int FIFTEEN_MINUTE = 15 * ONE_MINUTE;
    /**
     * 半个小时
     */
    public static final int HALF_HOUR = 30 * ONE_MINUTE;
    /**
     * 1个小时
     */
    public static final int ONE_HOUR = 2 * HALF_HOUR;
    /**
     * 半天
     */
    public static final int HALF_DAY = 12 * ONE_HOUR;
    /**
     * 1天
     */
    public static final int ONE_DAY = 24 * ONE_HOUR;
    /**
     * 1周
     */
    public static final int ONE_WEEK = 7 * ONE_DAY;


}
