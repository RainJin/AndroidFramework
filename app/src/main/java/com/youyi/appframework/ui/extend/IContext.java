package com.youyi.appframework.ui.extend;

import android.app.Application;

import com.youyi.appframework.AppContext;

/**
 * 获取AppContext接口
 * Created by Rain on 2016/3/4.
 */
public interface IContext {

    /**
     * 获取上下文环境
     * @return
     */
    Application getAppContext();
}
