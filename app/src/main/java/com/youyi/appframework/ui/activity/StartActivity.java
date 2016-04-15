package com.youyi.appframework.ui.activity;

import android.support.v4.app.Fragment;
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
        Fragment mFragment = new StartFragment();
        ft.add(R.id.content_fragment, new StartFragment());
        ft.commit();
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

}
