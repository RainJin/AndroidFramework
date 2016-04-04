package com.youyi.net.core.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;
import com.youyi.net.core.http.request.OkHttpStack;
import com.youyi.net.core.http.request.YYRequest;

/**
 * http请求管理器
 * Created by Rain on 2016/2/4.
 */
public class YYService {
    /**
     * Default on-disk cache directory.
     */
    private static final String DEFAULT_CACHE_DIR = "volley";
    private static YYService _instance = null;

    private Context mContext = null;

    private RequestQueue mRequestQueue = null;

    private int mTaskId = 0x100;

    private YYService(Context context) {
        mContext = context;
//        mRequestQueue = Volley.newRequestQueue(mContext);
        mRequestQueue = Volley.newRequestQueue(mContext, new OkHttpStack(new OkHttpClient()));
    }

    public static YYService createInstance(Context context) {
        if (_instance == null) {
            _instance = new YYService(context);
        }
        return _instance;
    }

    public static YYService getInstance() {
        return _instance;
    }

    /**
     * 添加获取数据请求
     *
     * @param task
     */
    public int addService(final YYRequest task) {
        if (task == null)
            return -1;

        int taskId = ++mTaskId;
        task.setmId(taskId);
        //通过volley 获取网络数据
        mRequestQueue.add(task.getmRequest());
        return taskId;
    }


    /**
     * 取消任务
     *
     * @param task
     */
    public void cancelTask(YYRequest task) {
        if (task != null) task.cancel();
    }

    /**
     * 取消所有任务
     */
    public void cancelAllTask() {
        mRequestQueue.cancelAll(YYRequest.TAG);
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        mRequestQueue.getCache().clear();
    }

    /**
     * 获取缓存路径
     */
    public String getCachePath() {
        return mContext.getCacheDir().getAbsolutePath() + DEFAULT_CACHE_DIR;
    }


}
