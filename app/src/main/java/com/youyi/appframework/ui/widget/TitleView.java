package com.youyi.appframework.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youyi.appframework.R;

/**
 * Created by Rain on 2016/4/14.
 */
public class TitleView extends RelativeLayout {

    protected Context mContext;
    private TextView mTopname;
    private TextView mBottomname;
    private TextView mUnreadMsg;
    private ImageView mLeftIcon;
    private View mBg;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
        setListener();
    }


    private void initView() {
        View.inflate(mContext, getLayoutRes(), this);
        mTopname = (TextView) findViewById(R.id.chat_title_topname);
        mBottomname = (TextView) findViewById(R.id.chat_title_bottomname);
        mUnreadMsg = (TextView) findViewById(R.id.chat_title_unreadmsg_text);
        mLeftIcon = (ImageView) findViewById(R.id.public_title_left_icon);
        mBg = findViewById(R.id.public_title_bg);
    }

    private void setListener() {
        findViewById(R.id.chat_title_unreadmsg_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) mContext).finish();
            }
        });
    }

    public int getLayoutRes() {
        return R.layout.title_view;
    }

    public void setUnreadMsgCount(int count) {
        if (count <= 0) {
            mUnreadMsg.setText("消息");
        } else {
            mUnreadMsg.setText("消息(" + count + ")");
        }
    }

    public void setLeftIcon(int resid) {
        mLeftIcon.setImageResource(resid);
    }

    public void setLeftIconVisible(int visible) {
        mLeftIcon.setVisibility(visible);
    }

    public void setLeftTextVisible(int visible) {
        mUnreadMsg.setVisibility(visible);
    }

    public void setTitlename(String name) {
        mTopname.setText(name);
        mBottomname.setVisibility(GONE);
    }

    public void setTitleBgColor() {
        setLeftIconVisible(GONE);
        setLeftTextVisible(GONE);
        setTitleBg(getResources().getColor(R.color.white));
        setTitleTextColor(getResources().getColor(R.color.text_black));
    }

    public void setTitleBgWhite() {
        setLeftIcon(R.mipmap.nav_icon_back);
        setTitleBg(getResources().getColor(R.color.white));
        setTitleTextColor(getResources().getColor(R.color.black));
        setLeftTextVisible(GONE);
    }

    public void setTitleBg(int bgcolor) {
        mBg.setBackgroundColor(bgcolor);
    }

    public void setTitleTextColor(int bgcolor) {
        mTopname.setTextColor(bgcolor);
    }

    public void setTopname(String topname) {
        mTopname.setText(topname);
    }

    public void setBottomname(String bottomname) {
        mBottomname.setText(bottomname);
    }
}
