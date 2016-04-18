package com.youyi.appframework.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.youyi.appframework.AppContext;
import com.youyi.appframework.common.UIHelper;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;


/**
 * Android 工具类
 * <p/>
 * Created by Rain on 2016/2/17.
 */
public class AndroidUtils {
    private static final String TAG = AndroidUtils.class.getName();

    /**
     * 获取屏幕高度
     *
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private static int windowWidth = -1;
    private static int windowHeight = -1;
    private static int statusBarHeight = -1;

    public static int getWindowWidth(Context context) {
        if (windowWidth == -1) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            windowWidth = dm.widthPixels;
        }

        return windowWidth;
    }

    public static int getWindowHeight(Context context) {
        if (windowHeight == -1) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            windowHeight = dm.heightPixels;
        }

        return windowHeight;
    }

    public static int[] getScreeSize() {
        WindowManager wmgr = (WindowManager) AppContext.getInstance().getSystemService(Context.WINDOW_SERVICE);

        int[] result = new int[2];
        result[0] = -1;
        result[1] = -1;

        if (wmgr != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            wmgr.getDefaultDisplay().getMetrics(displayMetrics);

            result[0] = displayMetrics.widthPixels;
            result[1] = displayMetrics.heightPixels;
            return result;
        }
        return result;
    }

    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight < 0) {
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            int sbar = 0;
            try {
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(x);
            } catch (Exception e1) {

            }
        }

        return statusBarHeight;
    }

    /**
     * 打开软键盘
     *
     * @param window
     */
    public static void showSoftKeyBoard(final Window window) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (window.getCurrentFocus() != null) {
                    InputMethodManager inputManager = (InputMethodManager) window.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInputFromInputMethod(window.getCurrentFocus().getWindowToken(), 0);
                }
            }
        }, 200);
    }

    /**
     * 关闭软键盘
     *
     * @param window
     */
    public static void hideSoftKeyBoard(final Window window) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (window.getCurrentFocus() != null) {
                    InputMethodManager inputManager = (InputMethodManager) window.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(window.getCurrentFocus().getWindowToken(), 0);
                }
            }
        }, 200);
    }

    /**
     * 安装apk
     *
     * @param apkFilePath
     */
    public static void installApk(Context ctx, String apkFilePath) {
        File apkfile = new File(apkFilePath);
        if (!apkfile.exists()) {
            UIHelper.toastMessage(ctx, "安装主程序失败，找不到主程序文件，请尝试重新更新。");
            return;
        }
        LogUtil.d(TAG, "install apk: " + apkfile.getPath());
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }


    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e(TAG, "程序包名无法找到", e);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取metaData信息
     */
    public static String getMetaData(Context context, String key) {
        String metaData = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            metaData = appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e(TAG, "获取metaData信息", e);
        } catch (Exception e) {

        }
        return metaData;
    }

    /**
     * 获取当前程序版本名称
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // Get the package info
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e(TAG, "获取当前程序版本名称", e);
        } catch (Exception e) {

        }
        return versionName;
    }

    /**
     * 获取程序版本名称
     */
    public static String getAppVersionName(Context context, String packageName) {
        String versionName;
        try {
            // Get the package info
            PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);
            versionName = pi.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil .d(TAG, "获取程序版本名称", e);
            return "";
        } catch (Exception e) {
            return "";
        }
        return versionName;
    }

    /**
     * 获取当前代码版本号
     */
    public static int getAppVersionCode(Context context) {
        int localVersion = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            localVersion = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e(TAG, "获取当前代码版本号", e);
        } catch (Exception e) {

        }
        return localVersion;
    }

    /**
     * 比较两个app版本号，看是否需要更新
     *
     * @param targetVersion
     * @param baseVersion
     * @return true if targetVersion > baseVersion,return true if haven't
     * install
     */
    public static boolean needToUpdate(String targetVersion, String baseVersion) {
        if (StringUtils.isEmpty(targetVersion)) {
            return false;// empty target, return false
        }
        if (StringUtils.isEmpty(baseVersion)) {
            return true;// not install, return true
        }
        List<String> targetItems = StringUtils.stringToList(targetVersion, StringUtils.VERSION_SEPERATOR);
        List<String> baseItems = StringUtils.stringToList(baseVersion, StringUtils.VERSION_SEPERATOR);
        LogUtil.d("targetItems", targetItems.toString());
        LogUtil.d("baseItems", baseItems.toString());

        if (CollectionUtils.isEmpty(targetItems) || CollectionUtils.isEmpty(baseItems)) {
            return false;
        }

        final int targetSize = targetItems.size();
        final int baseSize = baseItems.size();
        final int total = targetSize > baseSize ? targetSize : baseSize;

        for (int i = 0; i < total; i++) {
            int targetV = (i >= targetSize) ? 0 : Integer.parseInt(targetItems.get(i));
            int baseV = (i >= baseSize) ? 0 : Integer.parseInt(baseItems.get(i));
            if (targetV > baseV) {
                return true;
            }
            if (targetV < baseV) {
                return false;
            }

        }
        return false;
    }

    /**
     * 获取当前android手机型号
     */
    public static String getDriverModel() {
        String DeviceModel;
        DeviceModel = Build.MODEL;
        if (TextUtils.isEmpty(DeviceModel)) {
            return "";
        }
        return DeviceModel;
    }

    /**
     * 获取当前系统版本号
     */
    public static String getDriverVersionName() {
        String DeviceVersionName;
        DeviceVersionName = Build.VERSION.RELEASE;
        if (TextUtils.isEmpty(DeviceVersionName)) {
            return "";
        }
        return DeviceVersionName;
    }

    public static String getImei(Context context) {
        String imei = "";
        try {
            imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {

        }

        return imei;
    }


    /**
     * 获取当前系统版本名称
     */
    public static String getSystemVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 是否锁屏状态
     *
     * @return
     */
    public static boolean isKeyguard() {
        KeyguardManager mKeyguardManager = (KeyguardManager) AppContext.getInstance().getSystemService(Context.KEYGUARD_SERVICE);

        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }

    private static long lastClickTime;

    public static boolean isDoubleClick() {
        long time = System.currentTimeMillis() - lastClickTime;
        if (0 < time && time < 1000) {
            return true;
        }
        lastClickTime = System.currentTimeMillis();
        return false;
    }

}
