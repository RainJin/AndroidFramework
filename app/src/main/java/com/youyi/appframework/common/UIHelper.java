package com.youyi.appframework.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

import com.youyi.appframework.ui.activity.StartActivity;


/**
 * 界面辅助工具
 * <p/>
 * Created by Rain on 2016/3/4.
 */
public class UIHelper {
    private static Toast toast;

    /**
     * 弹出Toast消息
     *
     * @param charSequence
     */
    public static void toastMessage(Context context, CharSequence charSequence) {
        if (toast == null) {
            toast = Toast.makeText(context, charSequence, Toast.LENGTH_SHORT);
        } else {
            toast.setText(charSequence);
        }
        toast.show();
    }

    /**
     * 弹出Toast消息
     *
     * @param charSequence
     */
    public static void toastMessageMiddle(Context context, CharSequence charSequence) {
        if (toast == null) {
            toast = Toast.makeText(context, charSequence, Toast.LENGTH_SHORT);
        } else {
            toast.setText(charSequence);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 弹出Toast消息
     *
     * @param resId
     */
    public static void toastMessageMiddle(Context context, int resId) {
        toastMessageMiddle(context, context.getResources().getText(resId));
    }

    /**
     * 资源ID信息显示
     *
     * @param context
     * @param resId
     */
    public static void toastMessage(Context context, int resId) {
        toastMessage(context, context.getResources().getText(resId));
    }

    /**
     * 指定消息显示时间
     *
     * @param context
     * @param charSequence
     * @param time
     */
    public static void toastMessage(Context context, CharSequence charSequence, int time) {
        if (toast == null) {
            toast = Toast.makeText(context, charSequence, time);
        } else {
            toast.setText(charSequence);
        }
        toast.show();
    }

    /**
     * 提醒登录框
     * @param activity
     * @return
     */
    public static boolean showLoginDialog(final Context activity) {

        new AlertDialog
                .Builder(activity)
                .setTitle("提醒")
                .setMessage("您没有登录,请登录后再操作")
                .setPositiveButton("马上登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(activity, StartActivity.class);
                        activity.startActivity(intent);
                    }
                });

        return true;
    }



}
