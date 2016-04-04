package com.youyi.appframework.ui.extend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.youyi.appframework.R;
import com.youyi.appframework.ui.widget.SwipeLayout;


/**
 * Created by Rain on 2016/3/4.
 * 滑动返回基类
 */
public class SwipeActivity extends AppCompatActivity {

    private SwipeLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        swipeLayout = new SwipeLayout(this);
    }

    public void setSwipeAnyWhere(boolean swipeAnyWhere) {
        swipeLayout.setmSwipeAnyWhere(swipeAnyWhere);
    }

    public boolean isSwipeAnyWhere() {
        return swipeLayout.ismSwipeAnyWhere();
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        swipeLayout.setmSwipeEnabled(swipeEnabled);
    }

    public boolean isSwipeEnabled() {
        return swipeLayout.ismSwipeEnabled();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        swipeLayout.replaceLayer(this);
    }


    @Override
    public void finish() {
        if (swipeLayout.ismSwipeFinished()) {
            super.finish();
            overridePendingTransition(0, 0);
        } else {
            swipeLayout.cancelPotentialAnimation();
            super.finish();
            overridePendingTransition(0, R.anim.activity_right_out);
        }
    }


}
