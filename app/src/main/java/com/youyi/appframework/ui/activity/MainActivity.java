package com.youyi.appframework.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.youyi.appframework.AppContext;
import com.youyi.appframework.R;
import com.youyi.appframework.ui.extend.BaseActivity;

import java.util.HashMap;

/**
 * Created by Rain on 2016/2/17.
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getName();
    /**
     * 首页
     */
    public static final String TAB_HOME = "tab_home";
    /**
     * 产品
     */
    public static final String TAB_PRODUCT = "tab_product";
    /**
     * 发现
     */
    public static final String TAB_DISCOVERY = "tab_discovery";
    /**
     * 我
     */
    public static final String TAB_ME = "tab_me";

    /**
     * 首页
     */
    public static final int TAB_HOME_POSITION = 0;
    /**
     * 产品
     */
    public static final int TAB_PRODUCT_POSITION = 1;
    /**
     * 发现
     */
    public static final int TAB_DISCOVERY_POSITION = 2;
    /**
     * 我
     */
    public static final int TAB_ME_POSITION = 3;

    /**
     * 主界面容器
     */
    private static FrameLayout vpContainer;

    /**
     * 底部TAB导航
     */
    private RadioGroup rgMainNav;

    /**
     * 当前显示卡页的ID
     */
    private int mCurSelectedTabId = -1;

    private int PAGER_COUNT = 4;

    /**
     * 底部4个tab
     */
    private RadioButton mHomeRb;
    private RadioButton mProductRb;
    private RadioButton mDiscoveryRb;
    private RadioButton mMeRb;

    FragmentManager mFragmentMgr;

    private final int[] TAB_ID = new int[]{R.id.rb_home,
            R.id.rb_product, R.id.rb_discovery, R.id.rb_me};

    private HashMap<String, Fragment> mTabFragment;

    private Fragment mLastFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (mTabFragment != null) {
                mTabFragment.clear();
            }
        }
        mTabFragment = new HashMap<>();
        super.onCreate(savedInstanceState);
        setSwipeEnabled(false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void beforeInitView() {
        setContentView(R.layout.activity_main);
        mFragmentMgr = this.getSupportFragmentManager();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void doRefresh() {

    }

    @Override
    public AppContext getAppContext() {
        return null;
    }
}
