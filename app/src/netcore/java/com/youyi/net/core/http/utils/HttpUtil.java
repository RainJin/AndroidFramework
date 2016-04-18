package com.youyi.net.core.http.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rain on 2016/2/16.
 */
public class HttpUtil {
    private static final String TAG = HttpUtil.class.getName();

    /**
     * Converts <code>params</code> 使用于URL参数格式化，或者body参数格式化
     */
    public static String encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(dealParamKey(entry.getKey()), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            LogUtil.d(TAG, encodedParams.toString());
            return encodedParams.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }

    /**
     * Converts <code>params</code> into an application/x-www-form-urlencoded encoded string.
     */
    public static byte[] encodeParametersToBytes(Map<String, String> params, String paramsEncoding) {
        if(params == null || params.isEmpty())
            return null;
        try {
            return encodeParameters(params, paramsEncoding).getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }

    /**
     * name[0]
     * to
     * name[]
     *
     * @param key
     * @return
     */
    public static String dealParamKey(String key) {
        String content = key;
        String regex = "\\[[0-9]\\d*\\]$";
        LogUtil.d(TAG, content);

        Matcher matcher = Pattern.compile(regex).matcher(content);
        if (matcher.find()) {
            content = content.replace(matcher.group(), "[]");
        }

        return content;
    }
}
