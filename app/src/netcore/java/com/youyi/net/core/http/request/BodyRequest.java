package com.youyi.net.core.http.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.youyi.net.core.http.YYDataListener;
import com.youyi.net.core.http.config.HttpMethod;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 主体请求类
 * Created by Rain on 2016/2/16.
 */
public class BodyRequest extends YYRequest {

    private byte[] mBytes = null;

    public BodyRequest(String mUrl, HttpMethod mMethod, HashMap<String, String> mHeaders, YYDataListener mListener) {
        super(mUrl, mMethod, mHeaders, mListener);
    }

    @Override
    public String getmContentType() {
        return "application/json; charset=" + getmParamsEncoding();
    }

    @Override
    protected Request buildRequest() {
        StringRequest request = new StringRequest(mMethod.getId(), getmUrl(), mResponseListener, mErrorListener) {
            /**
             * HEAD 参数
             * @return
             * @throws com.android.volley.AuthFailureError
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
                //mListener 不为空
                //status = [200|201|304]
                if (mListener != null && (response.statusCode == HttpStatus.SC_OK || response.statusCode == HttpStatus.SC_CREATED
                        || response.notModified)) {
                    mListener.header(response.headers);
                }

                return super.parseNetworkResponse(response);
            }

            @Override
            public String getBodyContentType() {
                return getmContentType();
            }
        };

        request.setRetryPolicy(getmRetryPolicy());
        request.setShouldCache(ismShouldCache());
        request.setTag(TAG);
        return request;
    }

    @Override
    public byte[] getRequestBody() {
        return mBytes;
    }

    public void setBody(byte[] mBytes) {
        this.mBytes = mBytes;
    }

    public void setBody(String bodyStr) {
        try {
            this.mBytes = bodyStr.getBytes(getmParamsEncoding());
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + getmParamsEncoding(), uee);
        }
    }

    public void setBody(JSONObject bodyJson) {
        try {
            this.mBytes = bodyJson.toString().getBytes(getmParamsEncoding());
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + getmParamsEncoding(), uee);
        }
    }

    public void setBody(JSONArray bodyJson) {
        try {
            this.mBytes = bodyJson.toString().getBytes(getmParamsEncoding());
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + getmParamsEncoding(), uee);
        }
    }


}
