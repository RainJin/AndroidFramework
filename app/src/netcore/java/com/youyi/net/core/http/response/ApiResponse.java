package com.youyi.net.core.http.response;

/**
 * Created by Rain on 2016/2/17.
 */
public class ApiResponse {
    private int code;
    private String response;

    public ApiResponse(String response, int code) {
        this.response = response;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", response='" + response + '\'' +
                '}';
    }
}
