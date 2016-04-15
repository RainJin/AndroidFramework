package com.youyi.appframework.ui.fragment;

import android.widget.TextView;

import com.youyi.appframework.R;
import com.youyi.appframework.ui.extend.BaseFragment;
import com.youyi.appframework.utils.AndroidUtils;

/**
 * Created by Rain on 2016/4/14.
 */
public class StartFragment extends BaseFragment {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_start;
    }

    @Override
    protected void initial() {
        initView();
        initData();
    }



    protected void initView() {
        TextView version = (TextView) findView().findViewById(R.id.tv_version);
        version.setText("当前版本：" + AndroidUtils.getAppVersionName(getActivity()));
    }
    protected void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }


}
