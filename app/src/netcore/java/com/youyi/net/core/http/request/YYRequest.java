package com.youyi.net.core.http.request;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.youyi.net.core.http.YYDataListener;
import com.youyi.net.core.http.config.Global;
import com.youyi.net.core.http.config.HttpMethod;
import com.youyi.net.core.http.response.HttpError;
import com.youyi.net.core.http.utils.GsonUtil;
import com.youyi.net.core.http.utils.LogUtil;
import com.youyi.net.core.http.utils.StringUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Rain on 2016/2/4.
 */
public abstract class YYRequest {

    public static String TAG = "YYRequest";

    /**
     * URL
     */
    protected String mUrl;

    /**
     * Method
     */
    protected HttpMethod mMethod;

    /**
     * 请求的头部
     */
    protected HashMap<String, String> mHeaders = new HashMap<String, String>();

    /**
     * 缓存有效时间
     * 默认有效时长为10分钟
     */
    protected int mCacheTime = 10 * 60 * 1000;

    /**
     * 超时时间
     */
    protected int mTimeout = Global.REQUEST_TIMEOUT_MS;

    /**
     * 最多重复次数
     */
    protected int mMaxRetries = Global.REQUEST_MAX_RETRIES;

    /**
     * 编码
     */
    protected String mParamsEncoding = Global.DEFAULT_PARAMS_ENCODING;


    protected String mContentType = Global.DEFAULT_CONTENTTYPE;


    private RetryPolicy mRetryPolicy = null;

    /**
     * 从网络上获取到的数据是否需要缓存,默认不缓存
     */
    private boolean mShouldCache = false;

    /**
     * 是否是刷新数据,如果是刷新数据则不读取缓存数据
     */
    private boolean mRefresh = false;

    protected YYDataListener mListener = null;

    protected Request mRequest = null;

    protected Response.Listener<String> mResponseListener = null;
    protected Response.ErrorListener mErrorListener = null;

    private Class mClazz = null;

    private int mId = 0;

    public YYRequest(String mUrl, HttpMethod mMethod, HashMap<String, String> mHeaders, YYDataListener mListener) {
        this.mUrl = mUrl;
        this.mMethod = mMethod;
        this.mHeaders = mHeaders;
        this.mListener = mListener;
    }


    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public HttpMethod getmMethod() {
        return mMethod;
    }

    public void setmMethod(HttpMethod mMethod) {
        this.mMethod = mMethod;
    }

    public int getmCacheTime() {
        return mCacheTime;
    }

    /**
     * 设置缓存有限时长
     *
     * @param mCacheTime
     */
    public void setmCacheTime(int mCacheTime) {
        this.mCacheTime = mCacheTime;
    }

    public int getmTimeout() {
        return mTimeout;
    }

    public void setmTimeout(int mTimeout) {
        this.mTimeout = mTimeout;
    }

    public int getmMaxRetries() {
        return mMaxRetries;
    }

    public void setmMaxRetries(int mMaxRetries) {
        this.mMaxRetries = mMaxRetries;
    }

    public String getmParamsEncoding() {
        return mParamsEncoding;
    }

    public void setmParamsEncoding(String mParamsEncoding) {
        this.mParamsEncoding = mParamsEncoding;
    }

    public String getmContentType() {
        return mContentType;
    }

    public void setmContentType(String mContentType) {
        this.mContentType = mContentType;
    }

    public RetryPolicy getmRetryPolicy() {
        if (mRetryPolicy == null)
            mRetryPolicy = new DefaultRetryPolicy(mTimeout, mMaxRetries, 2f);
        return mRetryPolicy;
    }

    public void setmRetryPolicy(RetryPolicy mRetryPolicy) {
        this.mRetryPolicy = mRetryPolicy;
    }

    public boolean ismShouldCache() {
        return mShouldCache;
    }

    public void setmShouldCache(boolean mShouldCache) {
        this.mShouldCache = mShouldCache;
    }

    public boolean ismRefresh() {
        return mRefresh;
    }

    public void setmRefresh(boolean mRefresh) {
        this.mRefresh = mRefresh;
    }

    public YYDataListener getmListener() {
        return mListener;
    }

    public void setmListener(YYDataListener mListener) {
        this.mListener = mListener;
    }

    public Class getType() {
        return mClazz;
    }

    public void setType(Class mClazz) {
        this.mClazz = mClazz;
    }

    /**
     * 获取任务id
     *
     * @return
     */
    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
        mListener.setId(mId);
    }

    public Request getmRequest() {
        if (mRequest == null) {
            mResponseListener = createResponse();
            mErrorListener = createErrorListener();
            mRequest = buildRequest();
        }
        return mRequest;
    }

    protected abstract Request buildRequest();

    /**
     * 创建响应监听器
     *
     * @return
     */
    private Response.Listener<String> createResponse() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (StringUtil.isEmpty(response)) {
                        HttpError httpError = new HttpError(HttpError.Error.RESPONSE_NULL.getCode(),
                                HttpError.Error.RESPONSE_NULL.getMessage());
                        mListener.fail(httpError);
                        return;
                    }

                    LogUtil.e(TAG, "response success " + response);
                    if (mClazz != null)
                        mListener.success(GsonUtil.fromJson(response, mClazz));
                    else
                        mListener.success(response);
                } finally {
                    destroy();
                }

            }
        };

        return responseListener;
    }

    /**
     * 创建错误监听器
     *
     * @return
     */
    private Response.ErrorListener createErrorListener() {
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HttpError httpError = makeError(error);

                if (error.networkResponse != null && error.networkResponse.data != null) {
                    // 错误内容
                    int statusCode = error.networkResponse.statusCode;
                    String response = new String(error.networkResponse.data);
                    String message = null;
                    LogUtil.e("xxxxxxxxxxxxxx", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        message = jsonObject.optString("message");
                    } catch (Exception e) {
                    }
                    // 转换错误信息
                    if (StringUtil.isNotEmpty(message)) {
                        httpError = new HttpError(statusCode, message);
                    } else {
                        httpError = new HttpError(statusCode, response);
                    }
                }
                if (error.getCause() instanceof IOException
                        && error.getMessage().indexOf("No authentication challenges found") > -1) {
                    httpError.setCode(401);
                    httpError.setMessage(error.getMessage());
                }
                //判断是否失去Token而产生的401
/*                if(AccountManager.getInstance().isLogined() && httpError.getCode() == 401){
                    UserAuthenticChangeEvent event = new UserAuthenticChangeEvent();
                    event.type = UserAuthenticChangeEvent.TYPE_TOKEN_FAIL;
                    EventBus.getDefault().post(event);
                }*/

                try {
                    mListener.fail(httpError);
                } finally {
                    destroy();
                }
            }
        };
        return errorListener;
    }

    /**
     * 产生一个错误信息
     *
     * @param error
     * @return
     */
    private HttpError makeError(VolleyError error) {
        HttpError httpError = new HttpError();
        //请求超时
        if (error instanceof TimeoutError) {
            httpError.setCode(HttpError.Error.TIMEOUT_ERROR.getCode());
            httpError.setMessage(HttpError.Error.TIMEOUT_ERROR.getMessage());
        }
        //无法链接
        else if (error instanceof NoConnectionError) {
            httpError.setCode(HttpError.Error.NO_CONNECTION_ERROR.getCode());
            httpError.setMessage(HttpError.Error.NO_CONNECTION_ERROR.getMessage());
        }
        //身份验证失败
        else if (error instanceof AuthFailureError) {
            httpError.setCode(HttpError.Error.AUTH_FAILURE_ERROR.getCode());
            httpError.setMessage(HttpError.Error.AUTH_FAILURE_ERROR.getMessage());
        }
        //服务器错误
        else if (error instanceof ServerError) {
            httpError.setCode(HttpError.Error.SERVER_ERROR.getCode());
            httpError.setMessage(HttpError.Error.SERVER_ERROR.getMessage());
        }
        //网络错误
        else if (error instanceof NetworkError) {
            httpError.setCode(HttpError.Error.NETWORK_ERROR.getCode());
            httpError.setMessage(HttpError.Error.NETWORK_ERROR.getMessage());
        }
        //其他类型
        else {
            httpError.setCode(HttpError.Error.DEFUALT_ERROR.getCode());
            httpError.setMessage(HttpError.Error.DEFUALT_ERROR.getMessage());
        }
        return httpError;
    }


    /**
     * HTTP BODY
     * 处理HTTP中的主体内容
     *
     * @return body
     */
    public abstract byte[] getRequestBody();

    /**
     * 取消任务
     */
    public void cancel() {
        mRequest.cancel();
        destroy();
    }

    /**
     * 销毁任务
     */
    public void destroy() {
        mResponseListener = null;
        mErrorListener = null;
        mRequest = null;
    }


}
