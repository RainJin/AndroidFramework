package com.youyi.net.core.http.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.youyi.net.core.http.YYDataListener;
import com.youyi.net.core.http.config.HttpMethod;
import com.youyi.net.core.http.utils.LogUtil;

import org.apache.http.HttpStatus;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 该类相比较于Xutils ，性能、文件大小还有待测试，断点续传.......
 * 上传文件
 * Created by Rain on 2016/2/17.
 */
public class UploadFileRequest extends YYRequest {
    private MultipartEntity entity = new MultipartEntity();

    public UploadFileRequest(String mUrl, HttpMethod mMethod, HashMap<String, String> mHeaders, YYDataListener mListener) {
        super(mUrl, mMethod, mHeaders, mListener);
    }

    /**
     * 添加参数
     *
     * @param key
     * @param value
     * @throws UnsupportedEncodingException
     */
    public void addStringParam(String key, String value) throws UnsupportedEncodingException {
        entity.addPart(key, new StringBody(value, Charset.forName("utf-8")));
    }

    /**
     * 添加文件
     *
     * @param key
     * @param file
     */
    public void addFileParam(String key, File file) {
        entity.addPart(key, new FileBody(file));
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
    public String getmContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getRequestBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            LogUtil.e("UploadFileRequest", "IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }
}
