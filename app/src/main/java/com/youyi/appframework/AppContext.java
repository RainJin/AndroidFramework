package com.youyi.appframework;

import android.app.Application;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;


/**
 * Created by Rain on 2016/2/17.
 * App上下文环境
 */
public class AppContext extends Application {

    private static AppContext me = null;

    public static Handler mainHandler;

    public AppContext() {
        this.me = this;
    }

    public static AppContext getInstance() {
        return me;
    }

    /**
     * 应用启动
     */
    @Override
    public void onCreate() {
        mainHandler = new Handler(Looper.getMainLooper());
        super.onCreate();
        me = this;
        //初始化UM
//        initUMTrace();
        //初始化LeanCloud
//        initLeanCloud();
        //初始化一些网络接口或单例

        //注册关键模块
        registerModule();
        //初始化图片加载器
//        initImageLoader();

        //TODO 暂时关闭
        //AlarmsManager.getInstance().setRemindTimerTask();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
            //开启WebView的debug模式
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    /**
     * 初始化友盟统计设置
     */
/*    public static void initUMTrace() {
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setSessionContinueMillis(60 * 1000);

        //加密日志
        AnalyticsConfig.enableEncrypt(true);

        //友盟log打印控制
        Log.LOG = Constants.DEV_MODE;

    }*/

    /**
     * 初始化LeanCloud
     */
/*    public static void initLeanCloud() {
        // 初始化应用信息
        AVAnalytics.setAnalyticsEnabled(Constants.ANALYTICS_ENABLE);
        AVOSCloud.setDebugLogEnabled(Constants.AVOS_LOG_ENABLE);
        AVOSCloud.initialize(me, Constants.AVOS_APP_ID, Constants.AVOS_APP_KEY);

        // 启用崩溃错误统计
        AVAnalytics.enableCrashReport(me.getApplicationContext(), true);
        // 设置session有效期为
        AVAnalytics.setSessionContinueMillis(60 * 1000);
        // 准备接收PUSH消息
        installAVOSInstallIdAndSubscribe();

        //每次启动更新基本信息
        LeanCloudManager.registerLeanCloud(me);
    }*/

    /**
     * 配置LeanCloud Subscribe
     */
/*    private static void installAVOSInstallIdAndSubscribe() {
        // set a default callback. It's necessary for current SDK.
        // 在v2.0以后的版本请务必添加这段代码，以避免推送无法成功达到客户端的问题
        PushService.setDefaultPushCallback(me, StartActivity.class);
        PushService.subscribe(me, "public", StartActivity.class);
        PushService.subscribe(me, "private", StartActivity.class);
        PushService.subscribe(me, "protected", StartActivity.class);

    }*/

    /**
     * 应用退出
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        onExit();
    }

    /**
     * 应用内存不足
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //清理内存中的图片缓存
//        ImageLoader.getInstance().clearMemoryCache();
    }

    /**
     * 注册公共模块
     */
    private void registerModule() {
        Config.register(this);
    }

    /**Config
     * 初始化图片加载器
     */
/*    private void initImageLoader() {
        int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        int cacheSize = 1024 * 1024 * memClass / 10;  //系统可用内存的1/10

        // File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .threadPoolSize(2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(cacheSize))
                .memoryCacheSize(cacheSize)
                .diskCache(new UnlimitedDiscCache(new File(Config.getAppImagesPath()))) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(500)
                .defaultDisplayImageOptions(ImageOptionFactory.getDefaultImageOptions())
                .build();
        ImageLoader.getInstance().init(configuration);
    }*/

    /**
     * 应用退出，由AppManager回调
     */
    public void onExit() {
        //清理内存中的图片缓存
//        ImageLoader.getInstance().clearMemoryCache();
        //销毁一些单例

    }

}
