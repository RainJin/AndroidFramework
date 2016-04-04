package com.youyi.appframework.ui.widget;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.youyi.appframework.ui.extend.BaseActivity;


public class ErrorViewBuilder {
	public static View buildErrorView(final BaseActivity activity, String errmsg, int code) {
		TextView textView = new TextView(activity);
		textView.setWidth(LayoutParams.FILL_PARENT);
		textView.setHeight(LayoutParams.FILL_PARENT);
		textView.setGravity(Gravity.CENTER);
		textView.setText(errmsg+"\n"+"请点我更新");
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.hideErrorMsgAndRefresh();
				activity.doRefresh();
				
			}
		});
		return textView;
	}
}
