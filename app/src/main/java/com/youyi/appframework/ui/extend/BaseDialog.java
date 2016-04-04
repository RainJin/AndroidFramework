package com.youyi.appframework.ui.extend;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.youyi.appframework.AppContext;
import com.youyi.appframework.R;
import com.youyi.appframework.utils.LogUtil;


/**
 * 对话框 - 基类
 * <p/>
 * Created by Rain on 2016/3/4.
 */
public abstract class BaseDialog extends Dialog implements IContext {
    private static final String TAG = BaseDialog.class.getName();

    public BaseDialog(Context context) {
        this(context, R.style.customDialog);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeInitView();
        initView();
        initListener();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(TAG, "onStart");

        // 返回通过Action
        /*
        View actionBack = findViewById(R.id.action_back);
        if (actionBack != null) {
            actionBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        */
    }


    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d(TAG, "onStop");
    }

    /**
     * 对话框大小
     *
     * @param witdh
     * @param height
     */
    public void setSize(int witdh, int height) {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = witdh;
        lp.height = height;
        dialogWindow.setAttributes(lp);

        LogUtil.d(TAG, "onSizeChanged. W: " + witdh + ", H:" + height);
    }

    /**
     * 获取上下文环境
     *
     * @return
     */
    @Override
    public AppContext getAppContext() {
        return (AppContext) getOwnerActivity().getApplication();
    }

    /**
     * 加载layout xml
     */
    protected abstract void beforeInitView();

    /**
     * 加载UI
     */
    protected abstract void initView();

    /**
     * 监听控件
     */
    protected abstract void initListener();

    /**
     * 加载网络数据
     */
    protected abstract void initData();
}
