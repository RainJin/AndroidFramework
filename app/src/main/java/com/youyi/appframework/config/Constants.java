package com.youyi.appframework.config;

import com.youyi.appframework.BuildConfig;

/**
 * 常量
 * Created by Rain on 2016/2/1.
 */
public class Constants {

    /**
     * 开发环境配置
     */
    public static final boolean DEV_MODE = BuildConfig.DEBUG;
//    public static final boolean DEV_MODE = false;

    /**
     * 是否使用友盟的统计
     */
    public final static boolean UMEMNG_ENABLE = true;

    /**
     * 分页每次返回条数
     */
    public final static int PER_PAGE = 10;

}
