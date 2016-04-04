package com.youyi.appframework.utils;

import com.youyi.appframework.BuildConfig;

import org.apache.http.Header;

public class LogUtil {

    public static final boolean isPrintLog = BuildConfig.DEBUG;

    public static void println(String msg) {
        if (isPrintLog) {
            System.out.println(msg == null ? "" : msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isPrintLog) {
            android.util.Log.i(tag, msg == null ? "" : msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (isPrintLog) {
            android.util.Log.i(tag, msg == null ? "" : msg, tr);
        }
    }

    public static void d(String msg) {
        if (isPrintLog) {
            android.util.Log.d("BASETAG", msg == null ? "" : msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isPrintLog) {
            android.util.Log.d(tag, msg == null ? "" : msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (isPrintLog) {
            android.util.Log.d(tag, msg == null ? "" : msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (isPrintLog) {
            android.util.Log.e(tag, msg == null ? "" : msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (isPrintLog) {
            android.util.Log.e(tag, msg == null ? "" : msg, tr);
        }
    }

    public static void v(String tag, String msg) {
        if (isPrintLog) {
            android.util.Log.v(tag, msg == null ? "" : msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (isPrintLog) {
            android.util.Log.v(tag, msg == null ? "" : msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (isPrintLog) {
            android.util.Log.w(tag, msg == null ? "" : msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (isPrintLog) {
            android.util.Log.w(tag, msg == null ? "" : msg, tr);
        }
    }

    public static void h(String tag, String hintmsg, Header[] headers) {
        if (isPrintLog && headers != null) {
            // HeaderElement he[] = null;
            int k = 0;
            StringBuilder sb = new StringBuilder();
            for (Header h : headers) {
                sb.setLength(0);
                sb.append(hintmsg + " H" + (k++) + " " + h.getName() + " : "
                        + h.getValue());
                // he = h.getElements();
                // if (he != null){
                // sb.append(" he-> ");
                // i = 0;
                // for (HeaderElement e:he){
                // if (i > 0)
                // sb.append(",");
                // sb.append(e.getName()+"="+e.getValue());
                // i ++ ;
                // }
                // }
                LogUtil.v(tag, sb.toString());
            }
        }
    }
}
