package com.youyi.appframework.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.youyi.appframework.R;
import com.youyi.appframework.ui.extend.BaseActivity;
import com.youyi.appframework.ui.fragment.StartFragment;

/**
 * Created by Rain on 2016/3/30.
 */
public class StartActivity extends BaseActivity {

    private static final String TAG = StartActivity.class.getSimpleName();

    @Override
    protected void beforeInitView() {
        setContentView(R.layout.activity_start);
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_fragment, new StartFragment());
        ft.commit();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        //设置统计和启动接口
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doRefresh() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeEnabled(false);
    }

}
