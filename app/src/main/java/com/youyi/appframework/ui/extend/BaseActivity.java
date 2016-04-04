package com.youyi.appframework.ui.extend;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.youyi.appframework.AppContext;
import com.youyi.appframework.AppManager;
import com.youyi.appframework.R;
import com.youyi.appframework.ui.dialog.CommAlertDialog;
import com.youyi.appframework.ui.dialog.LoadingDialog;
import com.youyi.appframework.ui.widget.ErrorViewBuilder;
import com.youyi.appframework.utils.LogUtil;
import com.youyi.appframework.utils.NetworkHelper;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Rain on 2016/2/17.
 * 基本Activity，用于继承使用
 * 支持向左滑动手势返回
 */
public abstract class BaseActivity extends SwipeActivity implements IContext {
    private static final String TAG = BaseActivity.class.getName();
    private Thread mUiThread;
    private boolean mNetworkAvailable = false;
    private Handler mHandler = new MyHandler();
    /**
     * 没网络显示error view
     */
    private View mErrorView;
    private NetworkHelper.NetworkConnectivityListener mNetworkConnectivityListener;
    /**
     * 用于该Activity的Message Box, 延迟实例化
     */
    private CommAlertDialog mAlertDialog;
    /**
     * Activity是否在前台运行：激活状态
     */
    private static boolean isActive = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, this.getClass().getSimpleName() + "onCreate()");
        super.onCreate(savedInstanceState);
        this.mUiThread = Looper.getMainLooper().getThread();
        AppManager.getInstance().addActivity(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        LogUtil.d(TAG, this.getClass().getSimpleName() + "onPostCreate()");
        super.onPostCreate(savedInstanceState);
        // 注册网络监听网络服务
        registerNetworkConnectivityListener();
        beforeInitView();
        initView();
        initListener();
        initData();
    }

    /**
     * 判断网络是否连接的
     *
     * @return
     */
    protected boolean isNetworkAvailable() {
        return NetworkHelper.isNetworkAvailable(this);
    }

    private void onNetworkAvailableInner(NetworkInfo ni) {
        onNetworkAvailable(ni);
    }

    /**
     * 网络可用时回调方法
     *
     * @param ni
     */
    protected void onNetworkAvailable(NetworkInfo ni) {
    }

    /**
     * 网络不可用时回调方法
     */
    protected void onNetworkUnavailable() {
    }

    /**
     * 注册网连接监听器 当网络接通或者断开时会收到通知
     */
    private void registerNetworkConnectivityListener() {
        if (this.mNetworkConnectivityListener == null) {
            this.mNetworkConnectivityListener = new NetworkHelper.NetworkConnectivityListener(this);
            this.mNetworkConnectivityListener.registerHandler(this.mHandler, new NetworkHelper.NetworkChangedNotifier() {
                public void onAnyDataConnectionChanged(NetworkHelper.NetworkConnectivityListener sender, int state) {
                }

                public void onNetworkChanged(NetworkHelper.NetworkConnectivityListener sender, NetworkInfo ni) {
                    if (BaseActivity.this.mNetworkConnectivityListener.isNetworkAvailable()) {
                        if (!BaseActivity.this.mNetworkAvailable) {
                            BaseActivity.this.mNetworkAvailable = true;
                            BaseActivity.this.onNetworkAvailableInner(NetworkHelper.getActiveNetworkInfo(BaseActivity.this));
                        }
                    } else if (BaseActivity.this.mNetworkAvailable) {
                        BaseActivity.this.onNetworkUnavailable();
                        BaseActivity.this.mNetworkAvailable = false;
                    }
                }
            });
            this.mNetworkConnectivityListener.startListening();
        }
    }


    protected abstract void beforeInitView();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    /**
     * 网络连接错误，点击刷新网络请求页面
     */
    public abstract void doRefresh();

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(TAG, this.getClass().getSimpleName() + " onResume");
        if (!isActive) {
            isActive = true;
            LogUtil.d("Activity状态", "从后台————→前台");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        super.onPause();
        LogUtil.d(TAG, this.getClass().getSimpleName() + " onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.mNetworkConnectivityListener != null) {
            this.mNetworkConnectivityListener.stopListening();
        }
        if (!isAppOnForeground() && isActive) {
            isActive = false;
            LogUtil.d("Activity状态", "从前台————→后台");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (this.mNetworkConnectivityListener != null) {
            this.mNetworkConnectivityListener.stopListening();
        }
        AppManager.getInstance().finishActivity(this);
    }

    /**
     * 结束当前activity
     */
    @Override
    public void finish() {
        Activity activity = AppManager.getInstance().getRootActivity(this);
        if (activity == this)
            super.finish();
        else
            activity.finish();
        System.gc();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 按下键盘上返回按钮
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            onBackTransition();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    protected void onBackTransition() {
        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
    }

    protected void onGoTransition() {
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        onGoTransition();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        onGoTransition();
    }

    @Override
    public void onBackPressed() {
        hideLoadingIndicator();
        setResult(RESULT_OK);
        finish();
    }

    /**
     * 显示loading页面
     */
    public void showLoadingIndicator() {
        LoadingDialog.show(BaseActivity.this, getResources().getString(R.string.loading_text));

        //没有网络,则提示没有网络
        if (!isNetworkAvailable()) {
            this.mHandler.removeMessages(5);
            Message msg = this.mHandler.obtainMessage(5);
            this.mHandler.sendMessageDelayed(msg, 5000L);
        }
    }

    /**
     * 隐藏loading页面
     */
    public void hideLoadingIndicator() {
        LoadingDialog.dispose();
    }

    /**
     * 取得通用提醒dialog
     *
     * @return
     */
    public synchronized CommAlertDialog getAlertDialog() {
        if (mAlertDialog == null) {
            synchronized (BaseActivity.class) {
                if (mAlertDialog == null) {
                    mAlertDialog = new CommAlertDialog(this, R.style.TransparentDialog);
                }
            }
        }
        return mAlertDialog;
    }

    /**
     * 显示可点击刷新的错误页面
     *
     * @param errmsg
     * @param code
     */
    protected void showErrorMsgAndRefresh(String errmsg, int code) {
        if (!isUIThread()) {
            Message msg = this.mHandler.obtainMessage(3, code, 0, errmsg);
            msg.sendToTarget();
            return;
        }
        if (this.mErrorView != null) {
            ViewGroup vg = (ViewGroup) this.mErrorView.getParent();
            if (vg != null) {
                vg.removeView(this.mErrorView);
            }
        }

        View v = ErrorViewBuilder.buildErrorView(this, errmsg, code);
        View contentView = getContentView();
        contentView.setVisibility(View.GONE);
        ViewGroup vp = (ViewGroup) contentView.getParent();
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, -1);
        vp.addView(v, lp);
        this.mErrorView = v;
    }


    /**
     * 隐藏错误信息
     */
    public void hideErrorMsgAndRefresh() {
        if (!isUIThread()) {
            Message msg = this.mHandler.obtainMessage(4);
            msg.sendToTarget();
            return;
        }
        if (this.mErrorView != null) {
            View contentView = getContentView();
            //显示内容的view
            contentView.setVisibility(View.VISIBLE);
            ViewGroup vp = (ViewGroup) contentView.getParent();
            //移除错误信息的view
            vp.removeView(this.mErrorView);
            this.mErrorView = null;
        }
    }


    protected View getContentView() {
        return getWindow().getDecorView();
    }


    /**
     * 获取上下文环境
     *
     * @return
     */
    @Override
    public AppContext getAppContext() {
        return (AppContext) getApplication();
    }

    private final class MyHandler extends Handler {
        public MyHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            if (AppManager.getInstance().getRootActivity(BaseActivity.this).isFinishing()) {
                super.handleMessage(msg);
                return;
            }

            switch (msg.what) {
                case 1: //显示loading页面
                    BaseActivity.this.showLoadingIndicator();
                    break;
                case 2:
                    BaseActivity.this.hideLoadingIndicator();
                    break;
                case 3: //显示可点击刷新的错误页面
                    BaseActivity.this.showErrorMsgAndRefresh((String) msg.obj, msg.arg1);
                    break;
                case 4:
                    BaseActivity.this.hideErrorMsgAndRefresh();
                    break;
                case 5:   //网络无法连接问题
                    if (!BaseActivity.this.isNetworkAvailable()) {
                        BaseActivity.this.hideLoadingIndicator();
                        // BaseActivity.this.showErrorMsgAndRefresh("当前无网络，请检查网络设置", -200);
                        // UIHelper.toastMessage(BaseActivity.this, "无网络，请检查网络设置");

                    }
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }


    /**
     * 判断当前线和是不是ui线程
     *
     * @return
     */
    public boolean isUIThread() {
        Thread thread = Thread.currentThread();
        return this.mUiThread.getId() == thread.getId();
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    /**
     * Token失效处理
     * @param event
     */
/*    public void onEventMainThread(UserAuthenticChangeEvent event) {
        switch (event.type) {
            case UserAuthenticChangeEvent.TYPE_TOKEN_FAIL:
                AccountManager.getInstance().logout();
                AccountManager.getInstance().updateAccount(AccountManager.NameKey.SSOID, "");
                ErrorHandler.preProcessError(this);
                break;
        }
    }*/


}
