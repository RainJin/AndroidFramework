package com.youyi.net.core.http;

import com.youyi.net.core.http.response.HttpError;

import java.util.Map;

/**
 * Created by Rain on 2016/2/16.
 */
public interface YYDataListener {

    void setId(int id);

    void header(Map<String, String> headers);

    void success(Object data);

    void fail(HttpError error);
}


