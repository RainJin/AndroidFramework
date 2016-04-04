package com.youyi.net.core.http.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.youyi.net.core.http.YYDataListener;
import com.youyi.net.core.http.config.HttpMethod;
import com.youyi.net.core.http.utils.HttpUtil;

import org.apache.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Http请求
 * url, method, headerMap, postParams
 * Created by Rain on 2016/2/4.
 */
public class HttpRequest extends YYRequest {

    /**
     * 请求参数
     */
    private Map<String, String> mRequestParams = new HashMap<String, String>();

    public HttpRequest(String mUrl, HttpMethod mMethod, HashMap<String, String> mHeaders, YYDataListener mListener) {
        super(mUrl, mMethod, mHeaders, mListener);
    }

    /**
     * isUrlRequest GET、HEAD、 DELETE
     *
     * @return
     */
    private boolean isUrlRequest() {
        return this.mRequestParams.size() > 0
                && (getmMethod() == HttpMethod.GET || getmMethod() == HttpMethod.HEAD || getmMethod() == HttpMethod.DELETE);
    }

    /**
     * @return the url with params
     */
    public String getUrl() {
        if (isUrlRequest()) {
            if (mUrl.contains("?")) {
                mUrl += "&";
            } else {
                mUrl += "?";
            }
            return mUrl += HttpUtil.encodeParameters(mRequestParams, getmParamsEncoding());
        }

        return mUrl;
    }

    /**
     * name=value
     * if array
     * name[0]=value
     * name[1]=value
     *
     * @param key
     * @param value
     */
    public void addParam(String key, String value) {
        this.mRequestParams.put(key, value);
    }

    /**
     * name[0]=value1
     * name[1]=value2
     *
     * @param key
     * @param values
     */
    public void addParam(String key, String... values) {
        for (int i = 0; i < values.length; i++) {
            this.mRequestParams.put(key + "[" + i + "]", values[i]);
        }
    }

    @Override
    protected Request buildRequest() {
        StringRequest request = new StringRequest(mMethod.getId(), getUrl(), mResponseListener, mErrorListener) {
            /**
             * HEAD 参数
             * @return
             * @throws AuthFailureError
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return mHeaders;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return getRequestBody();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (mListener != null && (response.statusCode == HttpStatus.SC_OK || response.statusCode == HttpStatus.SC_CREATED || response.notModified)) {
                    mListener.header(response.headers);
                }
                return super.parseNetworkResponse(response);
            }

            @Override
            public String getBodyContentType() {
                return super.getBodyContentType();
            }

        };
        request.setRetryPolicy(getmRetryPolicy());
        request.setShouldCache(ismShouldCache());
        request.setTag(TAG);
        return request;
    }

    /**
     * HTTP BODY
     * 处理HTTP中的主体内容
     * 包括POST和PUT参数，以及一些主体内容
     *
     * @return body
     */
    @Override
    public byte[] getRequestBody() {
        if (getmRequestParams() != null && getmRequestParams().size() > 0 && !isUrlRequest()) {
            return HttpUtil.encodeParametersToBytes(getmRequestParams(), getmParamsEncoding());
        }
        return null;
    }

    /**
     * @return the params body request
     */
    public Map<String, String> getmRequestParams() {
        if (isUrlRequest()) {
            return Collections.emptyMap();
        }
        return mRequestParams;
    }

    public void setmRequestParams(Map<String, String> mRequestParams) {
        this.mRequestParams = mRequestParams;
    }

}
